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
operator|.
name|language
package|;
end_package

begin_import
import|import
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
name|Representation
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
name|ws
operator|.
name|transfer
operator|.
name|dialect
operator|.
name|fragment
operator|.
name|ExpressionType
import|;
end_import

begin_comment
comment|/**  * Interface for FragmentDialect languages.  */
end_comment

begin_interface
specifier|public
interface|interface
name|FragmentDialectLanguage
block|{
comment|/**      * Returns fragment of resource, which is described by expression.      * @param representation Resource, from which is fragment computed.      * @param expression Expression written in the language.      * @return It can return org.w3c.dom.Node | org.w3c.dom.NodeList | String      */
name|Object
name|getResourceFragment
parameter_list|(
name|Representation
name|representation
parameter_list|,
name|ExpressionType
name|expression
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

