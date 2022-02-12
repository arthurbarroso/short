(ns short.shared.backoffice.layout
  (:require [reagent.core :as r]
            [garden.core :refer [css]]))

(def sidebar-css
  (css [:.sidebar {:height "100vh"
                   :background "#FBFCFC "
                   :width "3%"
                   :min-width "36px"}
        [:ul {:padding "0"}]
        [:li {:list-style "none"}]
        [:.sidebar-header {:background "#33373A"
                           :color "#FBFCFC"
                           :height "42px"
                           :display "flex"
                           :align-items "center"
                           :justify-content "center"}]
        [:.sidebar-list {:padding "0 8%"}]
        [:.sidebar-list-item {:border-radius "8px"
                              :height "36px"
                              :display "flex"
                              :align-items "center"
                              :justify-content "center"
                              :color "#080A0B"
                              :text-decoration "none"}
         [:a {:text-decoration "none"
              :color "#080A0B"}]
         [:i {:width "18px"
              :height "16px"
              :cursor "pointer"}]]]))

(def layout-css
  (css [:.layout {:display "flex"
                  :box-sizing "border-box"
                  :-webkit-box-sizing "border-box"
                  :-moz-box-sizing "border-box"
                  :overflow "auto"
                  :height "100vh"}
        [:.layout-wrapper {:width "100%" :box-sizing "border-box"}
         [:.searchbar {:background "#fbfcfc"
                       :height "42px"
                       :display "flex"
                       :align-items "center"
                       :padding "0 1%"}
          [:.icon {:width "36px" :color "#080A0B"}]
          [:.search-input {:width "100%"
                           :background "#fbfcfc"
                           :border "none"
                           :color "#66686A"}]]]]))

(defn sidebar []
  [:div {:class "sidebar"}
   [:div {:class "sidebar-header"}
    [:i {:class "fas fa-home"}]]
   [:ul {:class "sidebar-list"}
    [:div {:style {:height "48px"}}]
    [:li {:class "sidebar-list-item"}
     [:i {:class "fas fa-store"
          :title "products"}]]
    [:li {:class "sidebar-list-item"}
     [:i {:class "fas fa-sign-out-alt"
          :title "log out"}]]]])

(defn layout []
  ^{:key "panel"}
  [:<>
   [:div {:class "layout"}
    [sidebar]
    [:div {:class "layout-wrapper"}
     (into [:<>]
           (r/children (r/current-component)))]]])
