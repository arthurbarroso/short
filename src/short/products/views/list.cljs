(ns short.products.views.list
  (:require [short.backoffice.template :as template]
            [re-frame.core :as re-frame]
            [short.backoffice.subs :as subs]
            [short.backoffice.events :as events]
            [short.ui.text :as text]
            [short.ui.button :as button]
            [short.ui.table :as table]
            [short.products.views.create :as create]
            [reagent.core :as reagent]))

(defn navigate-to-create-variant [product-uuid product-title]
  (re-frame/dispatch [::events/navigate-to-product-variant-creation
                      {:product-id product-uuid
                       :product-name product-title}]))

(defn list-view []
  (let [products (re-frame/subscribe [::subs/products])
        product-data (re-frame/subscribe [::subs/product-form-values])
        modal-open? (reagent/atom false)]
    (fn []
      [template/layout
       ^{:key "panel"}
       [:<>
        [create/modal modal-open? product-data]
        [:<>
         [:div.product-list-header
          [text/typography {:text "PRODUCT LIST"
                            :variant "h2"}]
          [button/button {:text "Create product"
                          :on-click #(create/open-modal modal-open?)}]]
         [:div.table-wrapper
          [table/table {:columns ["Title" "Price" "Variants" "SKU"
                                  "Slug" "Active" "Add variant"]
                        :items @products
                        :item-keys [{:key :product/title}
                                    {:key :product/price}
                                    {:key :product/variant
                                     :fun count}
                                    {:key :product/sku}
                                    {:key :product/slug}
                                    {:key :product/active
                                     :fun str}
                                    {:key "button-create"
                                     :button-key "create-variant"
                                     :button {:text "Create variant"
                                              :function
                                              #(navigate-to-create-variant (-> % :product/uuid)
                                                                           (-> % :product/title))}}]
                        :key :product/uuid}]]]]])))
