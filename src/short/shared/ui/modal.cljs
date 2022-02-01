(ns short.shared.ui.modal
  (:require ["react-modal" :as Modal]
            [reagent.core :as r]))

(def custom-modal-css
  {:content {:top "50%"
             :left "50%"
             :right "auto"
             :bottom "auto"
             :marginRight "-50%"
             :transform "translate(-50%, -50%)"
             :width "50%"}})

(defn open-modal [modal-open?]
  (reset! modal-open? true))

(defn close-modal [modal-open?]
  (reset! modal-open? false))

(defn modal []
  (let [props (r/props (r/current-component))
        modal-open? (:modal-open? props)]
    [:> Modal {:isOpen @modal-open?
               :style custom-modal-css
               :onRequestClose #(close-modal modal-open?)}
     (into [:<>]
           (r/children (r/current-component)))]))
