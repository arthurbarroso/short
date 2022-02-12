(ns short.products.ui.views.list
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [short.products.ui.subs :as subs]
            [short.products.ui.events :as events]
            [short.variants.ui.views.create-modal :as variant-create]
            [short.products.ui.views.create :as create]
            [short.products.ui.views.edit :as edit]
            [short.shared.ui.text :as text]
            [short.shared.ui.button :as button]
            [short.shared.ui.table :as table]
            [short.shared.ui.modal :as modal]
            [short.shared.backoffice.layout :as layout]
            [clojure.string :as string]))

(defn navigate-to-create-variant [product-uuid product-title modal-open?]
  (re-frame/dispatch [::events/set-variant {:product-id product-uuid
                                            :product-name product-title}])
  (reset! modal-open? true))

(defn edit-product [product-data modal-open?]
  (re-frame/dispatch [::events/set-product-edit-form
                      {:title (:product/title product-data)
                       :sku (:product/sku product-data)
                       :price (:product/price product-data)
                       :uuid (:product/uuid product-data)
                       :slug (:product/slug product-data)
                       :active (:product/active product-data)}])
  (reset! modal-open? true))

(defn handle-search [data search-term]
  (if search-term
    (filter
     (fn [item]
       (string/includes? (:product/title item) search-term))
     data)
    data))

(defn list-view []
  (let [products (re-frame/subscribe [::subs/products])
        product-modal-open? (reagent/atom false)
        product-edit-modal-open? (reagent/atom false)
        variant-modal-open? (reagent/atom false)
        term (reagent/atom nil)]
    (fn []
      [:div
       ^{:key "panel"}
       [:<>
        [create/modal product-modal-open?]
        [edit/modal product-edit-modal-open?]
        [variant-create/modal variant-modal-open?]
        [layout/layout
         [:div.product-list-header {:style {:background "#FBFCFC"
                                            :padding "0 2%"}}
          [text/typography {:text "products / list"
                            :variant "p"}]
          [button/button {:icon "fa-solid fa-circle-plus"
                          :on-click #(modal/open-modal product-modal-open?)}]]
         [:div {:class "searchbar"}
          [:i {:class "fas fa-search icon"}]
          [:input {:class "search-input"
                   :placeholder "Search"
                   :on-change #(reset! term (-> % .-target .-value)) :value @term}]]
         [:div.table-wrapper {:style {:box-sizing "border-box"
                                      :height "90%"
                                      :bacgkround "#FBFCFC"}}
          [table/table {:columns ["Title" "Price" "Status (active)"]
                        :items (handle-search @products @term)
                        :item-keys [{:key :product/title}
                                    {:key :product/price
                                     :fun #(str "$ " %)}
                                    {:key :product/active
                                     :checkbox true}
                                    {:key "actions-variants"
                                     :actions [{:text "Edit"
                                                :title "Edit product"
                                                 ;; :icon "fa-regular fa-pen-to-square"
                                                :effect (fn [product]
                                                          (edit-product
                                                           product
                                                           product-edit-modal-open?))}
                                               {:text "Add variant"
                                                 ;; :icon "fa-regular fa-plus"
                                                :title "Add variant to product"
                                                :effect
                                                #(navigate-to-create-variant (-> % :product/uuid)
                                                                             (-> % :product/title)
                                                                             variant-modal-open?)}]}]
                        :key :product/uuid}]]]]])))
