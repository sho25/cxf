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
name|wsdl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Definition
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|WSDLException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|extensions
operator|.
name|ExtensionRegistry
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|factory
operator|.
name|WSDLFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|service
operator|.
name|model
operator|.
name|ServiceSchemaInfo
import|;
end_import

begin_comment
comment|/**  * WSDLManager  *  */
end_comment

begin_interface
specifier|public
interface|interface
name|WSDLManager
block|{
comment|/**      * Returns the ExtensionRegistry that the WSDLManager      * uses when reading WSDL files.   Users can use      * this to register their own extensors.      * @return the ExtensionRegistry      */
name|ExtensionRegistry
name|getExtensionRegistry
parameter_list|()
function_decl|;
comment|/**      * Returns the WSDLFactory that is used to read/write WSDL definitions      * @return the WSDLFactory      */
name|WSDLFactory
name|getWSDLFactory
parameter_list|()
function_decl|;
comment|/**      * Get the WSDL definition for the given URL.  Implementations      * may return a copy from a local cache or load a new copy       * from the URL.      * @param url - the location of the WSDL to load       * @return the wsdl definition      */
name|Definition
name|getDefinition
parameter_list|(
name|URL
name|url
parameter_list|)
throws|throws
name|WSDLException
function_decl|;
comment|/**      * Get the WSDL definition for the given URL.  Implementations      * may return a copy from a local cache or load a new copy       * from the URL.      * @param url - the location of the WSDL to load       * @return the wsdl definition      */
name|Definition
name|getDefinition
parameter_list|(
name|String
name|url
parameter_list|)
throws|throws
name|WSDLException
function_decl|;
comment|/**      * Get the WSDL definition for the given Element.  Implementations      * may return a copy from a local cache or load a new copy       * from the Element.      * @param element - the root element of the wsdl       * @return the wsdl definition      */
name|Definition
name|getDefinition
parameter_list|(
name|Element
name|element
parameter_list|)
throws|throws
name|WSDLException
function_decl|;
comment|/**      * Adds a definition into the cache for lookup later      * @param key      * @param wsdl      */
name|void
name|addDefinition
parameter_list|(
name|Object
name|key
parameter_list|,
name|Definition
name|wsdl
parameter_list|)
function_decl|;
comment|/**      *       * @return all Definitions in the map      */
name|Map
argument_list|<
name|Object
argument_list|,
name|Definition
argument_list|>
name|getDefinitions
parameter_list|()
function_decl|;
comment|/**      * This object will cache the schemas for a WSDL.      * @param wsdl      * @return the cache of all the schemas in the wsdl      */
name|ServiceSchemaInfo
name|getSchemasForDefinition
parameter_list|(
name|Definition
name|wsdl
parameter_list|)
function_decl|;
comment|/**      * Register a collection of schemas for a WSDL.      * @param wsdl      * @param schemas      */
name|void
name|putSchemasForDefinition
parameter_list|(
name|Definition
name|wsdl
parameter_list|,
name|ServiceSchemaInfo
name|schemas
parameter_list|)
function_decl|;
comment|/**      * If the definition is cached, remove it from the cache      * @param wsdl      */
name|void
name|removeDefinition
parameter_list|(
name|Definition
name|wsdl
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

