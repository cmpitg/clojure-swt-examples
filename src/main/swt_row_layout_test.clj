(ns main.swt-row-layout-test
  (:import (org.eclipse.swt.widgets Display Shell Button)
           (org.eclipse.swt.layout RowLayout)
           (org.eclipse.swt SWT)))

(defn gui-loop [display shell]
  (when-not (. shell (isDisposed))
    (if-not (. display (readAndDispatch))
      (. display (sleep)))
    (recur display shell)))

(defn run-gui []
  (let [display (Display.)
        shell   (doto (Shell. display)
                  (.setLayout (make-row-layout)))

        _ (doto (Button. shell (. SWT PUSH))
            (.setText "B1"))
        _ (doto (Button. shell (. SWT PUSH))
            (.setText "Wide Button 2"))
        _ (doto (Button. shell (. SWT PUSH))
            (.setText "Button 3"))]

    (doto shell
      (.pack)
      (.open))

    (gui-loop shell display)))

(defn -main [& args]
  (run-gui))
