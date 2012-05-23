;;; root/examples/org.eclipse.swt.snippets/src/org/eclipse/swt/snippets/Snippet108.java

(ns main.swt-play
  (:import [org.eclipse.swt SWT]
           [org.eclipse.swt.widgets Display Shell Button Text Label]
           [org.eclipse.swt.layout RowData RowLayout]
           [org.eclipse.swt.events SelectionAdapter]
           [org.eclipse.swt.custom StyledText]))

(defn gui-loop [display shell]
  (when-not (. shell (isDisposed))
    (if-not (. display (readAndDispatch))
      (. display (sleep)))
    (recur display shell)))

(def ^:dynamic *display* (Display.))
(def ^:dynamic *shell* (doto (Shell. *display*)
                         (.pack)
                         (.open)
                         (.setText "Something...")))

(def ^:dynamic *controls* (atom {}))

(defn eval-string [str]
  (try
    (let [res (eval (read-string str))]
      res)

    (catch Exception e
      (prn e))))

(defn make-eval-string [raw-str]
  (str "(do (in-ns 'main.swt-play) " raw-str ")"))

;; (deftype ClickedEvent [arg]
;;   SelectionAdapter)

;; (defn clicked-event [shell]
;;   (proxy [ClickedEvent] [shell]
;;     (widgetSelected [evt]
;;       (println (to-array (.getChildren shell))))))

(defn gui-main []
  (let [label (doto (Label. *shell* (. SWT NONE))
                (.setText "Enter your name:"))

        text (doto (Text. *shell* (. SWT BORDER))
               (.setLayoutData (RowData. 100 (. SWT DEFAULT))))

        ok-button (doto (Button. *shell* (. SWT PUSH))
                    (.setText "OK")
                    (.addSelectionListener
                     (proxy [SelectionAdapter] []
                       (widgetSelected [evt]
                         (eval-string (make-eval-string
                                       (. (@*controls* :text) (getText))))

                         (println "Receiver: "
                                  (make-eval-string (. (@*controls* :text)
                                                       (getText))))

                         ;; This works "(println @main.swt-play/*controls*)"
                         ;; This doesn't "(println @*controls*)"
                         ))))

        cancel-button (doto (Button. *shell* (. SWT PUSH))
                        (.setText "Cancel")
                        ;; (.addSelectionListener (clicked-event *shell*)
                        ;;  ;; (proxy [SelectionAdapter] []
                        ;;  ;;   (widgetSelected [evt]
                        ;;  ;;     (println (to-array (. *shell* (getChildren))))
                        ;;  ;;     ;; (. (@*controls* :display) (dispose))
                        ;;  ;;     ))
                        ;;  )
                        )]

    ;; (dosync (alter *controls* conj @*controls*
    ;;                {:text text :label label}))
    (swap! *controls* conj @*controls*
           {:shell *shell*
            :display *display*
            :text text
            :label label})

    (doto *shell*
      (.setDefaultButton ok-button)
      (.setLayout (new RowLayout))
      (.pack)
      (.open))

    (gui-loop *display* *shell*)
    (. *display* (dispose))))

(defn -main [& args]
  (gui-main))
