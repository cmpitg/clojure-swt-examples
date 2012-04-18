;;; FIXME: Refactor

(ns main.swt-styledtext-box-around
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

(defn make-style-range-border-solid [display]
  (let [sr (StyleRange.)]
    (set! (. sr borderColor) (.getSystemColor display
                                              (. SWT COLOR_RED)))
    (set! (. sr borderStyle) (. SWT BORDER_SOLID))
    sr))

(defn make-style-range-array [styles]
  (let [arr (make-array StyleRange 1)]
    (aset arr 0 (first styles))
    arr))

(defn all-occurrences [text substr]
  (let [helper (fn [res pos]
                 (if (not= -1 pos)
                   (recur (conj res pos)
                          (.indexOf text substr (inc pos)))
                   res))]
    (helper [] (.indexOf text substr))))

(defn draw-box [search-str text-area text styles]
  (let [style-array (make-style-range-array styles)]
    (doseq [pos (all-occurrences text search-str)]
      (.setStyleRanges text-area 0 0 (int-array 2 [pos (.length search-str)])
                       style-array))))

(defn gui-main []
  (let [search-str "box"
        contents (str "This demonstrates drawing a box\n"
                      "around every occurrence of the word\n"
                      "box in the StyledText")

        display (new Display)
        shell   (doto (new Shell display)
                  (.open)
                  (.setText "StyledText with box drawn around")
                  (.setLayout (FillLayout.)))

        styles [(make-style-range-border-solid display)]

        text-area (doto (StyledText. shell (. SWT NONE))
                    (.setText contents))]

    (draw-box search-str text-area contents styles)

    (doto shell
      (.pack)
      (.open))

    (gui-loop display shell)
    (.dispose display)))

(defn -main [& args]
  (gui-main)
  (println "Hello"))
