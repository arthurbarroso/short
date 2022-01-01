(ns short.backoffice.views.panel
  (:require [short.backoffice.template :as template]
            [re-frame.core :as re-frame]
            [short.backoffice.subs :as subs]
            [short.ui.text :as text]
            [short.ui.button :as button]
            ["react-modal" :as Modal]
            [reagent.core :as reagent]
            [short.backoffice.components.table :as table]))

(def columns [{:path [:product/title]
               :header "Title"
               :key :title}
              {:path [:product/price]
               :header "Price"
               :key :price}
              {:path [:product/sku]
               :header "SKU"
               :key :sku}
              {:path [:product/slug]
               :header "Slug"
               :key :slug}
              {:path [:product/active]
               :header "Active"
               :format #(str %)
               :key :active}
              {:path [:product/created_at]
               :header "Created at"
               :key :created_at}
              {:path [:product/variant]
               :header "Variant count"
               :key :variant
               :format #(count %)
               :attrs (fn [_data] {:style {:text-align "center" :display "block"}})}])

(defn open-modal [modal-open?]
  (reset! modal-open? true))

(defn close-modal [modal-open?]
  (reset! modal-open? false))

(defn panel-view []
  (let [products (re-frame/subscribe [::subs/products])
        modal-open? (reagent/atom false)]
    (fn []
      [template/layout
       ^{:key "panel"}
       [:<>
        [:> Modal {:isOpen @modal-open?
                   :onRequestClose #(close-modal modal-open?)}
         [:div
          [:h1 "hi"]]]
        [text/typography
         {:text "Product list"
          :variant "h2"}]
        [table/table {:columns columns
                      :items products
                      :key [:product/title]}]
        [button/button {:text "Create product"
                        :on-click #(open-modal modal-open?)}]]])))
