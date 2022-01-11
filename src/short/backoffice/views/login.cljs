(ns short.backoffice.views.login
  (:require [short.backoffice.template :as template]
            [short.ui.button :as button]
            [short.ui.input :as input]
            [short.ui.section :as section]
            [short.ui.label :as label]
            [short.ui.text :as text]
            [re-frame.core :as re-frame]
            [short.backoffice.events :as events]
            [short.backoffice.subs :as subs]
            [garden.core :refer [css]]))

(def login-container {:background "#FFF"})

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
        [:form {:class "login"
                :on-submit (fn [e]
                             (do (.preventDefault e)
                                 (login-handler @email @password)))}
         [text/typography {:text "Log in"
                           :variant "h2"}]
         [label/label {:text "E-mail address"}]
         [input/input {:type "email"
                       :placeholder "e-mail address"
                       :on-change
                       #(re-frame/dispatch [::events/change-email-input %])
                       :value @email}]
         [label/label {:text "Password"}]
         [input/input {:type "password"
                       :placeholder "password"
                       :on-change
                       #(re-frame/dispatch [::events/change-password-input %])
                       :value @password}]
         [button/button {:text "Log in"
                         :type "submit"
                         :extra-style "mt-3"
                         :on-click #()}]]]])))
