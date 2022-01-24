(ns short.products.views.create
  (:require ["react-modal" :as Modal]
            [re-frame.core :as re-frame]
            [short.backoffice.events :as events]
            [short.ui.label :as label]
            [short.ui.button :as button]
            [short.ui.input :as input]
            [short.shared :as shared]))

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

(defn create-product-handler [data modal-open?]
  (re-frame/dispatch [::events/create-product data])
  (close-modal modal-open?))

(defn create-product-modal
  [{:keys [title slug sku price]} modal-open?]
  [:<>
   [:form {:on-submit (fn [e]
                        (.preventDefault e)
                        (create-product-handler
                         {:sku sku
                          :active true
                          :slug slug
                          :title title
                          :price (js/parseFloat price)}
                         modal-open?))}
    [label/label {:text "Title"}]
    [input/input {:value title
                  :on-change
                  #(re-frame/dispatch
                    [::events/set-product-form-field-value :title %])
                  :placeholder "Product title"}]
    [label/label {:text "Slug"
                  :extra-style "mt-1"}]
    [input/input {:value slug
                  :on-change
                  #(re-frame/dispatch
                    [::events/set-product-form-field-value :slug (shared/slugify %)])
                  :placeholder "Product slug"}]
    [label/label {:text "Sku"
                  :extra-style "mt-1"}]
    [input/input {:value sku
                  :on-change
                  #(re-frame/dispatch
                    [::events/set-product-form-field-value :sku %])
                  :placeholder "Product sku"}]
    [label/label {:text "Price"
                  :extra-style "mt-1"}]
    [input/input {:value price
                  :type "number"
                  :on-change
                  #(re-frame/dispatch
                    [::events/set-product-form-field-value :price %])
                  :placeholder "Product price"}]
    [button/button-outlined {:text "Create"
                             :type "submit"
                             :extra-style "mt-2"
                             :on-click #()}]]])

(defn modal [modal-open? product-data]
  [:> Modal {:isOpen @modal-open?
             :style custom-modal-css
             :onRequestClose #(close-modal modal-open?)}
   [create-product-modal @product-data modal-open?]])
