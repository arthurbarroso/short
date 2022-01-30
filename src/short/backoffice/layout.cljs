(ns short.backoffice.layout
  (:require [short.ui.text :as text]
            [reagent.core :as r]
            [garden.core :refer [css]]))

(def sidebar-css
  (css [:.sidebar {:height "100vh"
                   :background "#4B5052"
                   :width "10%"}
        [:ul {:padding "0"}]
        [:li {:list-style "none"}]
        [:.sidebar-header {:background "#33373A"
                           :color "#BEC2C3"
                           :height "42px"
                           :display "flex"
                           :align-items "center"
                           :padding "0 8%"}]
        [:.sidebar-list {:padding "0 8%"}]
        [:.sidebar-list-item {:border-radius "8px"
                              :height "36px"
                              :display "flex"
                              :align-items "center"
                              :color "#9BA0A5"
                              :padding "0 4%"
                              :font-size "14px"
                              :text-decoration "none"}
         [:a {:text-decoration "none"
              :color "#9BA0A5"}]]
        [:.sidebar-list-item-active {:color "#FBFCFC"
                                     :background "#66686A"}
         [:a {:text-decoration "none"
              :color "#FBFCFC"}]]
        [:.icon {:width "36px"}]]))

(def layout-css
  (css [:.layout {:display "flex"
                  :box-sizing "border-box"
                  :-webkit-box-sizing "border-box"
                  :-moz-box-sizing "border-box"
                  :overflow "auto"
                  :height "100vh"}
        [:.layout-wrapper {:width "100%" :box-sizing "border-box"}
         [:.searchbar {:background "#080A0B"
                       :height "42px"
                       :display "flex"
                       :align-items "center"
                       :padding "0 1%"}
          [:.icon {:width "36px" :color "#FFF"}]
          [:.search-input {:width "100%"
                           :background "#080A0B"
                           :border "none"
                           :color "#66686A"}]]]]))

(defn sidebar []
  [:div {:class "sidebar"}
   [:div {:class "sidebar-header"}
    [:i {:class "fas fa-home icon"}]
    [text/typography {:text "short"
                      :variant "p"
                      :sizing "text-md"}]]
   [:ul {:class "sidebar-list"}
    [:li {:class "sidebar-list-item sidebar-list-item-active"}
     [:i {:class "fas fa-store icon"}]
     [:a {:href "/"} "Products"]]
    [:li {:class "sidebar-list-item"}
     [:i {:class "fas fa-sign-out-alt icon"}]
     [:p "Sign out"]]]])

(defn layout []
  (let [{:keys [search-fn search-val]} (r/props (r/current-component))]
    ^{:key "panel"}
    [:<>
     [:div {:class "layout"}
      [sidebar]
      [:div {:class "layout-wrapper"}
       [:div {:class "searchbar"}
        [:i {:class "fas fa-search icon"}]
        [:input {:class "search-input"
                 :placeholder "Search"
                 :on-change search-fn
                 :value search-val}]]
       (into [:<>]
             (r/children (r/current-component)))]]]))
