(ns main.swt-row-layout-test
  (:import (org.eclipse.swt.widgets Display Shell Button)
           (org.eclipse.swt.layout RowLayout)
           (org.eclipse.swt SWT)))

(defn gui-loop [display shell]
  (when-not (. shell (isDisposed))
    (if-not (. display (readAndDispatch))
      (. display (sleep)))
    (recur display shell))
  (. display (dispose)))

(defn make-row-layout []
  (let [layout (RowLayout.)]
    (set! (. layout wrap) true)
    layout))

(defn run-gui []
  (let [display (Display.)
        shell   (doto (Shell. display)
                  (.setLayout (make-row-layout))
                  (.setText "Basic RowLayout - Try resizing the window"))

        _ (doto (Button. shell (. SWT PUSH))
            (.setText "B1"))
        _ (doto (Button. shell (. SWT PUSH))
            (.setText "Wide Button 2"))
        _ (doto (Button. shell (. SWT PUSH))
            (.setText "Button 3"))]

    (doto shell
      (.pack)
      (.open))

    (println "Try resizing the window")
    (gui-loop display shell)))

(defn -main [& args]
  (run-gui))
