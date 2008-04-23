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
name|tools
operator|.
name|corba
operator|.
name|common
package|;
end_package

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
name|xml
operator|.
name|WSDLWriter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ibm
operator|.
name|wsdl
operator|.
name|DefinitionImpl
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ibm
operator|.
name|wsdl
operator|.
name|extensions
operator|.
name|PopulatedExtensionRegistry
import|;
end_import

begin_comment
comment|/**  * This class is a copy of the WSDLFactoryImpl from the wsdl4j implementation  * It overwrites the newWSDLWriter method to return a SchemaWriter   */
end_comment

begin_class
specifier|public
class|class
name|SchemaFactoryImpl
extends|extends
name|SchemaFactory
block|{
comment|/**      * Create a new instance of a Definition, with an instance of a      * PopulatedExtensionRegistry as its ExtensionRegistry.      *       * @see com.ibm.wsdl.extensions.PopulatedExtensionRegistry      */
specifier|public
name|Definition
name|newDefinition
parameter_list|()
block|{
name|Definition
name|def
init|=
operator|new
name|DefinitionImpl
argument_list|()
decl_stmt|;
name|ExtensionRegistry
name|extReg
init|=
name|newPopulatedExtensionRegistry
argument_list|()
decl_stmt|;
name|def
operator|.
name|setExtensionRegistry
argument_list|(
name|extReg
argument_list|)
expr_stmt|;
return|return
name|def
return|;
block|}
comment|/**      * Create a new instance of a SchemaWriter.      */
specifier|public
name|WSDLWriter
name|newWSDLWriter
parameter_list|()
block|{
return|return
operator|new
name|SchemaWriterImpl
argument_list|()
return|;
block|}
comment|/**      * Create a new instance of an ExtensionRegistry with pre-registered      * serializers/deserializers for the SOAP, HTTP and MIME extensions. Java      * extensionTypes are also mapped for all the SOAP, HTTP and MIME      * extensions.      */
specifier|public
name|ExtensionRegistry
name|newPopulatedExtensionRegistry
parameter_list|()
block|{
return|return
operator|new
name|PopulatedExtensionRegistry
argument_list|()
return|;
block|}
block|}
end_class

end_unit

