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
name|aegis
operator|.
name|type
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_comment
comment|/**  * @author<a href="mailto:dan@envoisolutions.com">Dan Diephouse</a>  * @since Feb 18, 2004  */
end_comment

begin_interface
specifier|public
interface|interface
name|TypeMapping
block|{
comment|/**      * Checks whether or not type mapping between specified XML type and Java      * type is registered.      *       * @param javaType Class of the Java type      * @param xmlType Qualified name of the XML data type      * @return boolean;<code>true</code> if type mapping between the      *         specified XML type and Java type is registered; otherwise      *<code>false</code>      */
name|boolean
name|isRegistered
parameter_list|(
name|Class
name|javaType
parameter_list|)
function_decl|;
name|boolean
name|isRegistered
parameter_list|(
name|QName
name|xmlType
parameter_list|)
function_decl|;
name|void
name|register
parameter_list|(
name|Class
name|javaType
parameter_list|,
name|QName
name|xmlType
parameter_list|,
name|Type
name|type
parameter_list|)
function_decl|;
name|void
name|register
parameter_list|(
name|Type
name|type
parameter_list|)
function_decl|;
name|void
name|removeType
parameter_list|(
name|Type
name|type
parameter_list|)
function_decl|;
name|Type
name|getType
parameter_list|(
name|Class
name|javaType
parameter_list|)
function_decl|;
name|Type
name|getType
parameter_list|(
name|QName
name|xmlType
parameter_list|)
function_decl|;
name|QName
name|getTypeQName
parameter_list|(
name|Class
name|clazz
parameter_list|)
function_decl|;
name|TypeCreator
name|getTypeCreator
parameter_list|()
function_decl|;
comment|/**      * Each mapping has a URI that identifies it.      * The mapping for a service uses the service URI.      * XML files can choose to only contribute mappings that match.      * @return the URI.      */
name|String
name|getMappingIdentifierURI
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

