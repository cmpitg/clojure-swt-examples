(ns main.swt-drag-drop-two-labels
  (:import [org.eclipse.swt SWT]
           [org.eclipse.swt.dnd DND Transfer TextTransfer DragSource DropTarget
            DragSourceListener DropTargetAdapter]
           [org.eclipse.swt.layout FillLayout]
           [org.eclipse.swt.widgets Display Shell Label]))

(declare gui-loop gui-main set-drag-drop)

(defn gui-loop [display shell]
  (when-not (.isDisposed shell)
    (if-not (.readAndDispatch display)
      (.sleep display))
    (recur display shell)))

(defn gui-main []
  (let [display (Display.)
        shell   (doto (Shell. display)
                  (.setLayout (FillLayout.)))
        label1 (doto (Label. shell (. SWT BORDER))
                 (.setText "TEXT"))
        label2 (Label. shell (. SWT BORDER))]
    (set-drag-drop label1)
    (set-drag-drop label2)

    (doto shell
      (.setSize 200 200)
      (.pack)
      (.open))

    (gui-loop display shell)
    (.dispose display)))

(defn set-drag-drop [label]
  (let [types (into-array Transfer [(TextTransfer/getInstance)])

        operations (bit-or (. DND DROP_MOVE)
                           (. DND DROP_COPY)
                           (. DND DROP_LINK))

        source (doto (DragSource. label operations)
                 (.setTransfer types)
                 (.addDragListener
                  (proxy [DragSourceListener] []
                    (dragStart [evt]
                      (set! (. evt doit) (not= 0 (.length (.getText label)))))
                    (dragSetData [evt]
                      (set! (. evt data) (.getText label)))
                    (dragFinished [evt]
                      (when (= (. evt detail) (. DND DROP_MOVE))
                        (.setText label ""))))))

        target (doto (DropTarget. label operations)
                 (.setTransfer types)
                 (.addDropListener
                  (proxy [DropTargetAdapter] []
                    (drop [evt]
                      (if (nil? (. evt data))
                        (set! (. evt detail) (. DND DROP_NONE))
                        (.setText label (. evt data)))))))]))

(defn -main [& args]
  (gui-main))