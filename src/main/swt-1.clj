(ns main.swt-1
  (:import (org.eclipse.swt.widgets Display Shell)))

(defn gui-loop [display shell]
  (when-not (. shell (isDisposed))
    (if-not (. display (readAndDispatch))
      (. display (sleep)))
    (recur display shell)))

(defn gui-main []
  (let [display (new Display)
        shell   (doto (new Shell display)
                  (.open)
                  (.setText "Thử phát xem"))]
    (gui-loop display shell)
    (. display (dispose))))

(defn -main [& args]
  (gui-main)
  (println "Hello"))
