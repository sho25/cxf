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
name|xkms
operator|.
name|ldap
operator|.
name|persistence
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|NamingEnumeration
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|NamingException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|directory
operator|.
name|Attribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|directory
operator|.
name|Attributes
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|directory
operator|.
name|SearchResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|xkms
operator|.
name|x509
operator|.
name|handlers
operator|.
name|LDAPSearch
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Tests need a real ldap server  */
end_comment

begin_class
specifier|public
class|class
name|LDAPSearchTest
block|{
annotation|@
name|Test
annotation|@
name|Ignore
specifier|public
name|void
name|testSearch
parameter_list|()
throws|throws
name|URISyntaxException
throws|,
name|NamingException
block|{
name|LDAPSearch
name|ldapSearch
init|=
operator|new
name|LDAPSearch
argument_list|(
literal|"ldap://localhost:2389"
argument_list|,
literal|"cn=Directory Manager"
argument_list|,
literal|"test"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|NamingEnumeration
argument_list|<
name|SearchResult
argument_list|>
name|answer
init|=
name|ldapSearch
operator|.
name|searchSubTree
argument_list|(
literal|"dc=example, dc=com"
argument_list|,
literal|"(cn=Testuser)"
argument_list|)
decl_stmt|;
while|while
condition|(
name|answer
operator|.
name|hasMore
argument_list|()
condition|)
block|{
name|SearchResult
name|sr
init|=
name|answer
operator|.
name|next
argument_list|()
decl_stmt|;
name|Attributes
name|attrs
init|=
name|sr
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
name|Attribute
name|cn
init|=
name|attrs
operator|.
name|get
argument_list|(
literal|"sn"
argument_list|)
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|cn
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

