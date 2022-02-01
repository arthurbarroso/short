(ns short.products.ui.views.list
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [short.products.ui.subs :as subs]
            [short.products.ui.events :as events]
            [short.variants.ui.views.create-modal :as variant-create]
            [short.products.ui.views.create :as create]
            [short.shared.ui.text :as text]
            [short.shared.ui.button :as button]
            [short.shared.ui.table :as table]
            [short.shared.ui.modal :as modal]
            [short.backoffice.layout :as layout]))

(defn navigate-to-create-variant [product-uuid product-title modal-open?]
  (re-frame/dispatch [::events/set-variant {:product-id product-uuid
                                            :product-name product-title}])
  (reset! modal-open? true))

(defn list-view []
  (let [products (re-frame/subscribe [::subs/products])
        product-modal-open? (reagent/atom false)
        variant-modal-open? (reagent/atom false)]
    (fn []
      [:div
       ^{:key "panel"}
       [:<>
        [create/modal product-modal-open?]
        [variant-create/modal variant-modal-open?]
        [layout/layout {:search-fn #()}
         [:div.product-list-header {:style {:background "#FBFCFC"
                                            :padding "0 2%"}}
          [text/typography {:text "products / list"
                            :variant "p"}]
          [button/button {:text "add"
                          :on-click #(modal/open-modal product-modal-open?)}]]
         [:div.table-wrapper {:style {:box-sizing "border-box"
                                      :height "90%"
                                      :bacgkround "#FBFCFC"}}
          [table/table {:columns ["" "Title" "Price" "Variants" "SKU"
                                  "Slug" "Variant"]
                        :items @products
                        :item-keys [{:key :product/active
                                     :checkbox true}
                                    {:key :product/title}
                                    {:key :product/price}
                                    {:key :product/variant
                                     :fun count}
                                    {:key :product/sku}
                                    {:key :product/slug}
                                    {:key "button-create"
                                     :button-key "create-variant"
                                     :button {:text "+"
                                              :function
                                              #(navigate-to-create-variant (-> % :product/uuid)
                                                                           (-> % :product/title)
                                                                           variant-modal-open?)}}]
                        :key :product/uuid}]]]]])))