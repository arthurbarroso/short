(ns short.products.client.views.create
  (:require ["react-modal" :as Modal]
            [re-frame.core :as re-frame]
            [short.ui.label :as label]
            [short.ui.button :as button]
            [short.ui.input :as input]
            [short.shared :as shared]
            [short.products.client.events :as events]
            [short.products.client.subs :as subs]
            [short.backoffice.components.modal :as modal-wrapper]))

(defn create-product-handler [data modal-open?]
  (re-frame/dispatch [::events/create-product data])
  (modal-wrapper/close-modal modal-open?))

(defn create-product-modal
  [modal-open?]
  (let [{:keys [title slug sku price]} (re-frame/subscribe [::subs/product-form-values])]
    (fn []
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
                                 :on-click #()}]]])))

(defn modal [modal-open?]
  [modal-wrapper/modal {:modal-open? modal-open?}
   [create-product-modal modal-open?]])
