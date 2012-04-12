(ns main.swt-button-toggle
  (:import [org.eclipse.swt.widgets Shell Display Control Button Listener]
           [org.eclipse.swt.layout FillLayout]
           [org.eclipse.swt.events SelectionAdapter]
           [org.eclipse.swt SWT]))

(defn gui-loop [display shell]
  "Main loop for SWT application"
  (when-not (.isDisposed shell)
    (if-not (.readAndDispatch display)
      (.sleep display))
    (recur display shell)))

(defn run-gui []
  (let [display (Display.)
        shell (doto (Shell. display)
                (.setLayout (FillLayout.)))

        toggle-listener (proxy [SelectionAdapter] []
                          (widgetSelected [evt]
                            (let [children (vec (.getChildren shell))]
                              (doseq [child children]
                                (if (and (not= (. evt widget) child)
                                         (instance? Button child)
                                         (.getSelection child))
                                  (.setSelection child false)))
                              (.setSelection (. evt widget) true))))

        _ (dotimes [i 20]
            (let [_ (doto (Button. shell (. SWT TOGGLE))
                      (.setText (str "B" i))
                      (.addSelectionListener toggle-listener)
                      (.setSelection (if (zero? i) true false)))]))]
    (doto shell
      (.pack)
      (.open))
    (gui-loop display shell)
    (.dispose display)))

(defn -main [& args]
  (run-gui))
