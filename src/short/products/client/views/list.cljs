(ns short.products.client.views.list
  (:require [re-frame.core :as re-frame]
            [short.products.client.subs :as subs]
            [short.ui.text :as text]
            [short.ui.button :as button]
            [short.ui.table :as table]
            [short.products.client.views.create :as create]
            [reagent.core :as reagent]
            [short.products.client.events :as events]
            [short.backoffice.layout :as layout]))

(defn navigate-to-create-variant [product-uuid product-title]
  (re-frame/dispatch [::events/navigate-to-product-variant-creation
                      {:product-id product-uuid
                       :product-name product-title}]))

(defn list-view []
  (let [products (re-frame/subscribe [::subs/products])
        product-data (re-frame/subscribe [::subs/product-form-values])
        modal-open? (reagent/atom false)]
    (fn []
      [:div
       ^{:key "panel"}
       [:<>
        [create/modal modal-open? product-data]
        [layout/layout {:search-fn #()}
         [:div.product-list-header {:style {:background "#FBFCFC"
                                            :padding "0 2%"}}
          [text/typography {:text "products"
                            :variant "p"}]
          [button/button {:text "+"
                          :on-click #(create/open-modal modal-open?)}]]
         [:div.table-wrapper {:style {:box-sizing "border-box"
                                      :height "80%"
                                      :bacgkround "#FBFCFC"}}
          [table/table {:columns ["" "Title" "Price" "Variants" "SKU"
                                  "Slug" "Add variant"]
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
                                     :button {:text "Create variant"
                                              :function
                                              #(navigate-to-create-variant (-> % :product/uuid)
                                                                           (-> % :product/title))}}]
                        :key :product/uuid}]]]]])))
