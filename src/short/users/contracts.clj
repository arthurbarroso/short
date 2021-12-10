(ns short.users.contracts)

(def UserData
  [:map
   [:email string?]
   [:password string?]
   [:password-confirmation string?]])

(def UserOut
  [:map
   [:user/email string?]
   [:user/uuid uuid?]
   [:user/created_at inst?]])
