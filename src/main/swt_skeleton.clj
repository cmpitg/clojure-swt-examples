(ns main.swt-skeleton
  (:import [org.eclipse.swt.widgets Display Shell]))

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
                  (.setText "Thử phát xem"))]
    (gui-loop display shell)
    (.dispose display)))

(defn -main [& args]
  (gui-main)
  (println "Hello"))
