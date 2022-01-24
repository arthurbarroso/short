(ns short.backoffice.components.image-picker
  (:require [reagent.core :as r]
            [short.ui.input :as input]
            [garden.core :refer [css]]
            [short.ui.text :as text]))

(def image-picker-input-css {:opacity 0
                             :width "0.1px"
                             :height "0.1px"
                             :position "absolute"})

(def image-picker-css
  (css [:.image-input
        [:.image-picker image-picker-input-css]
        [:label {:display "flex"
                 :align-items "center"
                 :cursor "pointer"
                 :padding "1px 2px"
                 :text-indent "5px"
                 :border "1.5px solid #D3D3D3"
                 :height "48px"
                 :color "#757575"
                 :border-radius "8px"}]]))

(defn handle-image-data [file file-key]
  {:file-type (.-type file)
   :file-size (-> file
                  (.-size)
                  (/ 1024)
                  .toFixed)
   :file-key (str file-key "-" (.-name file))
   :file-name (.-name file)})

(defn get-image-data [input-id]
  (let [el (.getElementById js/document input-id)
        file (aget (.-files el) 0)
        form-data (js/FormData.)
        _ (.append form-data "file" file)]
    {:form-data form-data
     :file file}))

(defn submit-image
  [input-id file-key]
  (let [{:keys [form-data file]} (get-image-data input-id)]
    (assoc
     (handle-image-data file file-key)
     :file form-data)))

(defn image-selector
  [input-id]
  (let [UPLOADED-IMAGE (r/atom nil)]
    (fn []
      [:div {:class "image-input"}
       [input/input {:type "file"
                     :id input-id
                     :value @UPLOADED-IMAGE
                     :on-change #(reset! UPLOADED-IMAGE %)
                     :accept "image/*"
                     :extra-style "image-picker"}]
       [:label {:htmlFor input-id}
        (if @UPLOADED-IMAGE
          "File added 🥳"
          "Upload image")]])))
