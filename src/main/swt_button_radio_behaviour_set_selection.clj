(ns main.swt-button-radio-behaviour-set-selection
  (:import [org.eclipse.swt.widgets Display Shell Button Listener]
           [org.eclipse.swt.layout RowLayout]
           [org.eclipse.swt SWT]))

(def ^:dynamic *controls* (atom {}))

(defn gui-loop [display shell]
  "Main loop for SWT application"
  (when-not (.isDisposed shell)
    (if-not (.readAndDispatch display)
      (.sleep display))
    (recur display shell)))

(defn gui-main []
  (let [display (new Display)
        shell   (doto (new Shell display)
                  (.open)
                  (.setLayout (RowLayout. (. SWT VERTICAL)))
                  (.setText "???"))

        buttons (vec (doall (for [i (range 8)]
                              (doto (Button. shell (. SWT RADIO))
                                (.setText (str "Button " i))
                                (.setSelection (if (zero? i) true false))))))

        _ (doto (Button. shell (. SWT PUSH))
            (.setText "Set selection to Button 4")
            (.addListener
             (. SWT Selection)
             (proxy [Listener] []
               (handleEvent [evt]
                 (doseq [button (@*controls* :buttons)]
                   (.setSelection button false))
                 (.setSelection ((@*controls* :buttons) 4) true)))))]

    (swap! *controls* conj @*controls*
           {:buttons buttons})

    (doto shell
      (.pack)
      (.open))

    (gui-loop display shell)
    (.dispose display)))

(defn -main [& args]
  (gui-main)
  (println "Hello"))
