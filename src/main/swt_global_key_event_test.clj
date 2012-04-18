(ns main.swt-global-key-event-test
  (:import [org.eclipse.swt SWT]
           [org.eclipse.swt.events KeyListener KeyEvent]
           [org.eclipse.swt.widgets Display Shell Event Listener]))

(defn mod1-key? [state-mask]
  (not (zero? (bit-and state-mask (. SWT MOD1)))))

(defn mod2-key? [state-mask]
  (not (zero? (bit-and state-mask (. SWT MOD2)))))

(defn mod3-key? [state-mask]
  (not (zero? (bit-and state-mask (. SWT MOD3)))))

(defn mod4-key? [state-mask]
  (not (zero? (bit-and state-mask (. SWT MOD4)))))

(defn shift-key? [state-mask]
  (not (zero? (bit-and state-mask (. SWT SHIFT)))))

(defn backspace? [key-code]
  (= (. SWT BS) key-code))

(defn escape? [key-code]
  (= (. SWT ESC) key-code))

(defn character? [key-code]
  (and (>= key-code 97)
       (<= key-code 122)))

(defn digit? [key-code]
  (and (>= key-code 48)
       (<= key-code 57)))

(def proxy-process-key
  (proxy [Listener] []
    (handleEvent [evt]
      (let [key-code    (. evt keyCode)
            state-mask  (. evt stateMask)

            modifier (cond (mod1-key? state-mask)
                           (str "Mod1 - keyCode: " key-code)

                           (mod2-key? state-mask)
                           (str "Mod2 - keyCode: " key-code)

                           (mod3-key? state-mask)
                           (str "Mod3 - keyCode: " key-code)

                           (shift-key? state-mask)
                           (str "Shift - keyCode: " key-code))

            main-key (cond (backspace? key-code)
                           (str "Backspace - keyCode: " key-code)

                           (escape? key-code)
                           (str "Escape - keyCode: " key-code)

                           (or (character? key-code)
                               (digit? key-code))
                           (str (. evt character) " - keyCode: "
                                key-code)

                           :else (str  "keyCode: " key-code))

            out (if (not= "" modifier)
                  (str modifier " " main-key)
                  (str main-key))]
        (println out)))))

(defn gui-loop [display shell]
  "Main loop for SWT application"
  (when-not (.isDisposed shell)
    (if-not (.readAndDispatch display)
      (.sleep display))
    (recur display shell)))

(defn gui-main []
  (let [display (Display.)
        shell (doto (Shell. display)
                (.setText "SWT Global KeyEvent Example")
                (.setBackground (.getSystemColor display
                                                 (. SWT COLOR_GREEN)))
                (.setSize 200 300))]
    (.addFilter display
                (. SWT KeyDown)
                proxy-process-key)
    (doto shell
      (.pack)
      (.open))
    (gui-loop display shell)
    (.dispose display)))

(defn -main [& agrs]
  (println "Hello")
  (gui-main))
