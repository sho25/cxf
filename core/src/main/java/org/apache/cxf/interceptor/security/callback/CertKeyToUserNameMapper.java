begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|interceptor
operator|.
name|security
operator|.
name|callback
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|Certificate
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|X509Certificate
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|InvalidNameException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|ldap
operator|.
name|LdapName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|ldap
operator|.
name|Rdn
import|;
end_import

begin_class
specifier|public
class|class
name|CertKeyToUserNameMapper
implements|implements
name|CertificateToNameMapper
block|{
specifier|private
name|String
name|key
decl_stmt|;
comment|/**      * Returns Subject DN from X509Certificate      *      * @param certificate      * @return Subject DN as a user name      */
annotation|@
name|Override
specifier|public
name|String
name|getUserName
parameter_list|(
name|Certificate
name|cert
parameter_list|)
block|{
name|X509Certificate
name|certificate
init|=
operator|(
name|X509Certificate
operator|)
name|cert
decl_stmt|;
name|String
name|dn
init|=
name|certificate
operator|.
name|getSubjectDN
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|LdapName
name|ldapDn
init|=
name|getLdapName
argument_list|(
name|dn
argument_list|)
decl_stmt|;
if|if
condition|(
name|key
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Must set a key"
argument_list|)
throw|;
block|}
for|for
control|(
name|Rdn
name|rdn
range|:
name|ldapDn
operator|.
name|getRdns
argument_list|()
control|)
block|{
if|if
condition|(
name|key
operator|.
name|equalsIgnoreCase
argument_list|(
name|rdn
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
return|return
operator|(
name|String
operator|)
name|rdn
operator|.
name|getValue
argument_list|()
return|;
block|}
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No "
operator|+
name|key
operator|+
literal|" key found in certificate DN: "
operator|+
name|dn
argument_list|)
throw|;
block|}
specifier|private
name|LdapName
name|getLdapName
parameter_list|(
name|String
name|dn
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|LdapName
argument_list|(
name|dn
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|InvalidNameException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid DN"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|setKey
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
block|}
block|}
end_class

end_unit

