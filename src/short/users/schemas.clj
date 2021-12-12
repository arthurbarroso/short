(ns short.users.schemas)

(def User
  [:map
   [:user/email string?]
   [:user/password string?]
   [:user/uuid uuid?]
   [:user/created_at inst?]
   [:user/active boolean?]])

(def UserTx
  [:map
   [:db-before :any]
   [:db-after :any]
   [:tx-data :any]
   [:tempids :map]])

(def ExistingUser
  [:map
   [:user/email string?]
   [:user/password string?]
   [:user/uuid uuid?]
   [:user/created_at inst?]
   [:user/active boolean?]
   [:db/id int?]])

(def TokenOut
  [:maybe
   [:map [:token string?]]])

(def UserQueryResult
  [:or
   [:vector
    [:vector
     ExistingUser]]
   [:vector empty?]])

(def CredentialsCheck
  [:map
   [:matches? boolean?]
   [:existing-user UserQueryResult]])
