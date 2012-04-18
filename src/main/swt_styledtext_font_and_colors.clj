(ns main.swt-styledtext-font-and-colors
  (:import [org.eclipse.swt.widgets Display Shell]
           [org.eclipse.swt.custom StyledText StyleRange]
           [org.eclipse.swt.layout FillLayout]
           [org.eclipse.swt SWT]))

(defn gui-loop [display shell]
  "Main loop for SWT application"
  (when-not (.isDisposed shell)
    (if-not (.readAndDispatch display)
      (.sleep display))
    (recur display shell)))

(defn make-style-range-first-part-bold []
  (let [sr (StyleRange.)]
    (set! (. sr start) 0)
    (set! (. sr length) 10)
    (set! (. sr fontStyle) (. SWT BOLD))
    sr))

(defn make-style-range-second-part-red [display]
  (let [sr (StyleRange.)]
    (set! (. sr start) 11)
    (set! (. sr length) 13)
    (set! (. sr foreground) (.getSystemColor display
                                             (. SWT COLOR_RED)))
    sr))

(defn make-style-range-third-part-blue-bg [display]
  (let [sr (StyleRange.)]
    (set! (. sr start) 25)
    (set! (. sr length) 13)
    (set! (. sr background) (.getSystemColor display
                                             (. SWT COLOR_BLUE)))
    sr))

(defn gui-main []
  (let [display (new Display)
        shell   (doto (new Shell display)
                  (.open)
                  (.setText "StyledText with Font and Colors")
                  (.setLayout (FillLayout.)))

        _ (doto (StyledText. shell (. SWT BORDER))
            (.setText "0123456789 ABCDEFGHIJKLM NOPQRSTUVWXYZ")
            (.setStyleRange (make-style-range-first-part-bold))
            (.setStyleRange (make-style-range-second-part-red display))
            (.setStyleRange (make-style-range-third-part-blue-bg display)))]
    (doto shell
      (.pack)
      (.open))
    (gui-loop display shell)
    (.dispose display)))

(defn -main [& args]
  (gui-main)
  (println "Hello"))
