(ns short.users.ui.views.login
  (:require [short.backoffice.template :as template]
            [short.shared.ui.button :as button]
            [short.shared.ui.input :as input]
            [short.shared.ui.section :as section]
            [short.shared.ui.label :as label]
            [short.shared.ui.text :as text]
            [re-frame.core :as re-frame]
            [short.users.ui.events :as events]
            [short.users.ui.subs :as subs]
            [garden.core :refer [css]]))

(def login-container {:width "50%"})

(def login-screen-css
  (css [:.login login-container]))

(defn login-handler [email password]
  (re-frame/dispatch [::events/login {:email email
                                      :password password}]))

(defn login-view []
  (let [email (re-frame/subscribe [::subs/email-input])
        password (re-frame/subscribe [::subs/password-input])]
    (fn []
      [template/layout
       ^{:key "login"}
       [section/section
        [:div {:style {:display "flex"
                       :flex-direction "column"
                       :width "100%"}}
         [text/typography {:text "short"
                           :variant "h1"
                           :sizing "text-xxxl"}]
         [:form {:class "login"
                 :on-submit (fn [e]
                              (do (.preventDefault e)
                                  (login-handler @email @password)))}
          [text/typography {:text "log in"
                            :variant "h2"}]
          [label/label {:text "e-mail address"}]
          [input/input {:type "email"
                        :placeholder "e-mail address"
                        :on-change
                        #(re-frame/dispatch [::events/change-email-input %])
                        :value @email}]
          [label/label {:text "password"}]
          [input/input {:type "password"
                        :placeholder "password"
                        :on-change
                        #(re-frame/dispatch [::events/change-password-input %])
                        :value @password}]
          [button/button {:text "Log in"
                          :type "submit"
                          :extra-style "mt-3"
                          :on-click #()}]]]]])))
