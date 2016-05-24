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
name|ws
operator|.
name|transfer
operator|.
name|dialect
operator|.
name|fragment
package|;
end_package

begin_comment
comment|/**  * Helper class for holding of constants.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|FragmentDialectConstants
block|{
specifier|public
specifier|static
specifier|final
name|String
name|FRAGMENT_2011_03_IRI
init|=
literal|"http://www.w3.org/2011/03/ws-fra"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|QNAME_LANGUAGE_IRI
init|=
literal|"http://www.w3.org/2011/03/ws-fra/QName"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|XPATH10_LANGUAGE_IRI
init|=
literal|"http://www.w3.org/2011/03/ws-fra/XPath10"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FRAGMENT_TEXT_NODE_NAME
init|=
literal|"TextNode"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FRAGMENT_ATTR_NODE_NAME
init|=
literal|"AttributeNode"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FRAGMENT_ATTR_NODE_NAME_ATTR
init|=
literal|"name"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FRAGMENT_MODE_REPLACE
init|=
literal|"http://www.w3.org/2011/03/ws-fra/Modes/Replace"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FRAGMENT_MODE_ADD
init|=
literal|"http://www.w3.org/2011/03/ws-fra/Modes/Add"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FRAGMENT_MODE_INSERT_BEFORE
init|=
literal|"http://www.w3.org/2011/03/ws-fra/Modes/InsertBefore"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FRAGMENT_MODE_INSERT_AFTER
init|=
literal|"http://www.w3.org/2011/03/ws-fra/Modes/InsertAfter"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FRAGMENT_MODE_REMOVE
init|=
literal|"http://www.w3.org/2011/03/ws-fra/Modes/Remove"
decl_stmt|;
specifier|private
name|FragmentDialectConstants
parameter_list|()
block|{              }
block|}
end_class

end_unit
