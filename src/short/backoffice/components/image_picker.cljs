(ns short.backoffice.components.image-picker
  (:require [reagent.core :as r]))

(defn handle-image-data [file file-key]
  {:file-type (.-type file)
   :file-size (-> file
                  (.-size)
                  (/ 1024)
                  .toFixed)
   :file-key file-key})

(defn submit-image
  [input-id file-key]
  (let [el (.getElementById js/document input-id)
        file (aget (.-files el) 0)
        form-data (js/FormData.)
        _ (.append form-data "file" file)]
    (assoc
     (handle-image-data file file-key)
     :file form-data)))

(defn image-selector
  [input-id]
  (let [UPLOADED-IMAGE (r/atom nil)]
    [:input {:type "file"
             :id input-id
             :on-change #(reset! UPLOADED-IMAGE %)
             :accept "image/*"}]))
