;;; root/examples/org.eclipse.swt.snippets/src/org/eclipse/swt/snippets/Snippet108.java

(ns main.swt-button-demo
  (:import [org.eclipse.swt SWT]
           [org.eclipse.swt.widgets Display Shell Button Text Label]
           [org.eclipse.swt.layout RowData RowLayout]
           [org.eclipse.swt.events SelectionAdapter]
))

(defn gui-loop [display shell]
  (when-not (. shell (isDisposed))
    (if-not (. display (readAndDispatch))
      (. display (sleep)))
    (recur display shell)))


(defn gui-main []
  (let [display (Display.)
        shell (doto (Shell. display)
                (.open)
                (.setText "Something..."))

        label (doto (Label. shell (. SWT NONE))
                (.setText "Enter your name:"))

        text (doto (Text. shell (. SWT BORDER))
               (.setLayoutData (RowData. 100 (. SWT DEFAULT))))

        ok-button (doto (Button. shell (. SWT PUSH))
                    (.setText "OK")
                    (.addSelectionListener (proxy [SelectionAdapter] []
                                             (widgetSelected [evt]
                                               (println (. text (getText)))))))

        cancel-button (doto (Button. shell (. SWT PUSH))
                        (.setText "Cancel")
                        (.addSelectionListener (proxy [SelectionAdapter] []
                                                 (widgetSelected [evt]
                                                   (println "Cancel")))))]

    (doto shell
      (.setDefaultButton cancel-button)
      (.setLayout (new RowLayout))
      (.pack)
      (.open))

    (gui-loop display shell)
    (. display (dispose))))

(defn -main [& args]
  (gui-main))
