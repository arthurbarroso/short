(ns short.backoffice.css
  (:require [short.ui.css :refer [ui-styles]]
            [short.backoffice.views.panel :as panel]))

(defn mount-style
  [style-text]
  (let [head (or (.-head js/document)
                 (aget (.getElementsByTagName js/document "head") 0))
        style-el (doto (.createElement js/document "style")
                   (-> .-type (set! "text/css"))
                   (.appendChild (.createTextNode js/document style-text)))]
    (.appendChild head style-el)))

(defn mount-ui-styles []
  (let [styles (str "\n" ui-styles "\n" panel/product-modal-styles)]
    (mount-style styles)))
