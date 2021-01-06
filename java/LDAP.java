        String myAcctName="CN=$yourName,OU=Users,OU=People,DC=dsc,DC=local"
        Hashtable ldapEnv = new Hashtable();
        ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        ldapEnv.put(Context.PROVIDER_URL,  "ldap://192.168.11.5:389");
        ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
        ldapEnv.put(Context.SECURITY_PRINCIPAL, myAcctName);
        ldapEnv.put(Context.SECURITY_CREDENTIALS, pass);
        def ctx = new InitialDirContext(ldapEnv);
        String searchFilter="(&(objectClass=user))";
        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        NamingEnumeration<SearchResult> results = ctx.search(userContextThing, searchFilter, searchControls);
        List users=[]
        while (results.hasMoreElements())
            users += results.nextElement()
        users
