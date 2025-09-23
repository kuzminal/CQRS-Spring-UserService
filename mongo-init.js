db.createUser(
    {
        user: "dbuser",
        pwd: "dbpassword",
        roles: [
            {
                role: "readWrite",
                db: "user_query_db"
            }
        ]
    }
);