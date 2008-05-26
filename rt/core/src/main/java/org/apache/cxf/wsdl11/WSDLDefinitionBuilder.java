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
name|wsdl11
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|StringTokenizer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|Import
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
name|extensions
operator|.
name|mime
operator|.
name|MIMEPart
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
name|javax
operator|.
name|wsdl
operator|.
name|xml
operator|.
name|WSDLReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
import|;
end_import

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
name|soap
operator|.
name|SOAPHeaderImpl
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
name|soap
operator|.
name|SOAPHeaderSerializer
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
name|Bus
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
name|BusException
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
name|catalog
operator|.
name|OASISCatalogManager
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
name|common
operator|.
name|i18n
operator|.
name|Message
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|common
operator|.
name|util
operator|.
name|PropertiesLoaderUtils
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
name|helpers
operator|.
name|CastUtils
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
name|wsdl
operator|.
name|JAXBExtensionHelper
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
name|wsdl
operator|.
name|WSDLBuilder
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
name|wsdl
operator|.
name|WSDLConstants
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
name|wsdl
operator|.
name|WSDLExtensibilityPlugin
import|;
end_import

begin_class
specifier|public
class|class
name|WSDLDefinitionBuilder
implements|implements
name|WSDLBuilder
argument_list|<
name|Definition
argument_list|>
block|{
specifier|protected
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|WSDLDefinitionBuilder
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EXTENSIONS_RESOURCE
init|=
literal|"META-INF/extensions.xml"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|WSDL_PLUGIN_RESOURCE
init|=
literal|"META-INF/wsdl.plugin.xml"
decl_stmt|;
specifier|protected
name|WSDLReader
name|wsdlReader
decl_stmt|;
specifier|protected
name|Definition
name|wsdlDefinition
decl_stmt|;
specifier|final
name|WSDLFactory
name|wsdlFactory
decl_stmt|;
specifier|final
name|ExtensionRegistry
name|registry
decl_stmt|;
specifier|private
name|List
argument_list|<
name|Definition
argument_list|>
name|importedDefinitions
init|=
operator|new
name|ArrayList
argument_list|<
name|Definition
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|WSDLExtensibilityPlugin
argument_list|>
name|wsdlPlugins
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|WSDLExtensibilityPlugin
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|public
name|WSDLDefinitionBuilder
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|this
operator|.
name|bus
operator|=
name|b
expr_stmt|;
block|}
specifier|public
name|WSDLDefinitionBuilder
parameter_list|()
block|{
try|try
block|{
name|wsdlFactory
operator|=
name|WSDLFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
name|registry
operator|=
name|wsdlFactory
operator|.
name|newPopulatedExtensionRegistry
argument_list|()
expr_stmt|;
name|QName
name|header11
init|=
operator|new
name|QName
argument_list|(
name|WSDLConstants
operator|.
name|NS_SOAP11
argument_list|,
literal|"header"
argument_list|)
decl_stmt|;
name|QName
name|header12
init|=
operator|new
name|QName
argument_list|(
name|WSDLConstants
operator|.
name|NS_SOAP12
argument_list|,
literal|"header"
argument_list|)
decl_stmt|;
name|registry
operator|.
name|registerDeserializer
argument_list|(
name|MIMEPart
operator|.
name|class
argument_list|,
name|header11
argument_list|,
operator|new
name|SOAPHeaderSerializer
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|registerSerializer
argument_list|(
name|MIMEPart
operator|.
name|class
argument_list|,
name|header11
argument_list|,
operator|new
name|SOAPHeaderSerializer
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|mapExtensionTypes
argument_list|(
name|MIMEPart
operator|.
name|class
argument_list|,
name|header11
argument_list|,
name|SOAPHeaderImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|registry
operator|.
name|registerDeserializer
argument_list|(
name|MIMEPart
operator|.
name|class
argument_list|,
name|header12
argument_list|,
operator|new
name|SOAPHeaderSerializer
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|registerSerializer
argument_list|(
name|MIMEPart
operator|.
name|class
argument_list|,
name|header12
argument_list|,
operator|new
name|SOAPHeaderSerializer
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|mapExtensionTypes
argument_list|(
name|MIMEPart
operator|.
name|class
argument_list|,
name|header12
argument_list|,
name|SOAPHeaderImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|registerInitialExtensions
argument_list|()
expr_stmt|;
name|wsdlReader
operator|=
name|wsdlFactory
operator|.
name|newWSDLReader
argument_list|()
expr_stmt|;
name|wsdlReader
operator|.
name|setExtensionRegistry
argument_list|(
name|registry
argument_list|)
expr_stmt|;
comment|// TODO enable the verbose if in verbose mode.
name|wsdlReader
operator|.
name|setFeature
argument_list|(
literal|"javax.wsdl.verbose"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|wsdlReader
operator|.
name|setFeature
argument_list|(
literal|"javax.wsdl.importDocuments"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|WSDLDefinitionBuilder
parameter_list|(
name|boolean
name|requirePlugins
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
if|if
condition|(
name|requirePlugins
condition|)
block|{
name|registerWSDLExtensibilityPlugins
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setBus
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|this
operator|.
name|bus
operator|=
name|b
expr_stmt|;
block|}
specifier|public
name|Definition
name|build
parameter_list|(
name|String
name|wsdlURL
parameter_list|)
block|{
name|parseWSDL
argument_list|(
name|wsdlURL
argument_list|)
expr_stmt|;
return|return
name|wsdlDefinition
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|protected
name|void
name|parseWSDL
parameter_list|(
name|String
name|wsdlURL
parameter_list|)
block|{
try|try
block|{
name|wsdlReader
operator|.
name|setExtensionRegistry
argument_list|(
name|registry
argument_list|)
expr_stmt|;
name|WSDLLocatorImpl
name|wsdlLocator
init|=
operator|new
name|WSDLLocatorImpl
argument_list|(
name|wsdlURL
argument_list|)
decl_stmt|;
name|wsdlLocator
operator|.
name|setCatalogResolver
argument_list|(
name|OASISCatalogManager
operator|.
name|getCatalogManager
argument_list|(
name|bus
argument_list|)
operator|.
name|getCatalog
argument_list|()
argument_list|)
expr_stmt|;
name|wsdlDefinition
operator|=
name|wsdlReader
operator|.
name|readWSDL
argument_list|(
name|wsdlLocator
argument_list|)
expr_stmt|;
name|parseImports
argument_list|(
name|wsdlDefinition
argument_list|)
expr_stmt|;
if|if
condition|(
name|wsdlDefinition
operator|.
name|getServices
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|Definition
name|def
range|:
name|importedDefinitions
control|)
block|{
name|Set
argument_list|<
name|QName
argument_list|>
name|services
init|=
name|def
operator|.
name|getServices
argument_list|()
operator|.
name|keySet
argument_list|()
decl_stmt|;
for|for
control|(
name|QName
name|sName
range|:
name|services
control|)
block|{
if|if
condition|(
operator|!
name|wsdlDefinition
operator|.
name|getServices
argument_list|()
operator|.
name|keySet
argument_list|()
operator|.
name|contains
argument_list|(
name|sName
argument_list|)
condition|)
block|{
name|wsdlDefinition
operator|.
name|getServices
argument_list|()
operator|.
name|put
argument_list|(
name|sName
argument_list|,
name|def
operator|.
name|getService
argument_list|(
name|sName
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|we
parameter_list|)
block|{
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"FAIL_TO_CREATE_WSDL_DEFINITION"
argument_list|,
name|LOG
argument_list|,
name|wsdlURL
argument_list|,
name|we
operator|.
name|getMessage
argument_list|()
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|WSDLRuntimeException
argument_list|(
name|msg
argument_list|,
name|we
argument_list|)
throw|;
block|}
block|}
specifier|public
specifier|static
name|Collection
argument_list|<
name|Import
argument_list|>
name|getImports
parameter_list|(
specifier|final
name|Definition
name|wsdlDef
parameter_list|)
block|{
name|Collection
argument_list|<
name|Import
argument_list|>
name|importList
init|=
operator|new
name|ArrayList
argument_list|<
name|Import
argument_list|>
argument_list|()
decl_stmt|;
name|Map
name|imports
init|=
name|wsdlDef
operator|.
name|getImports
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iter
init|=
name|imports
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|String
name|uri
init|=
operator|(
name|String
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Import
argument_list|>
name|lst
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
operator|)
name|imports
operator|.
name|get
argument_list|(
name|uri
argument_list|)
argument_list|)
decl_stmt|;
name|importList
operator|.
name|addAll
argument_list|(
name|lst
argument_list|)
expr_stmt|;
block|}
return|return
name|importList
return|;
block|}
specifier|private
name|void
name|parseImports
parameter_list|(
name|Definition
name|def
parameter_list|)
block|{
for|for
control|(
name|Import
name|impt
range|:
name|getImports
argument_list|(
name|def
argument_list|)
control|)
block|{
if|if
condition|(
operator|!
name|importedDefinitions
operator|.
name|contains
argument_list|(
name|impt
operator|.
name|getDefinition
argument_list|()
argument_list|)
condition|)
block|{
name|importedDefinitions
operator|.
name|add
argument_list|(
name|impt
operator|.
name|getDefinition
argument_list|()
argument_list|)
expr_stmt|;
name|parseImports
argument_list|(
name|impt
operator|.
name|getDefinition
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|List
argument_list|<
name|Definition
argument_list|>
name|getImportedDefinitions
parameter_list|()
block|{
return|return
name|importedDefinitions
return|;
block|}
specifier|private
name|void
name|registerInitialExtensions
parameter_list|()
throws|throws
name|BusException
block|{
name|Properties
name|initialExtensions
init|=
literal|null
decl_stmt|;
try|try
block|{
name|initialExtensions
operator|=
name|PropertiesLoaderUtils
operator|.
name|loadAllProperties
argument_list|(
name|EXTENSIONS_RESOURCE
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|BusException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
for|for
control|(
name|Iterator
name|it
init|=
name|initialExtensions
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|StringTokenizer
name|st
init|=
operator|new
name|StringTokenizer
argument_list|(
name|initialExtensions
operator|.
name|getProperty
argument_list|(
operator|(
name|String
operator|)
name|it
operator|.
name|next
argument_list|()
argument_list|)
argument_list|,
literal|"="
argument_list|)
decl_stmt|;
name|String
name|parentType
init|=
name|st
operator|.
name|nextToken
argument_list|()
decl_stmt|;
name|String
name|elementType
init|=
name|st
operator|.
name|nextToken
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Registering extension: "
operator|+
name|elementType
operator|+
literal|" for parent: "
operator|+
name|parentType
argument_list|)
expr_stmt|;
block|}
name|JAXBExtensionHelper
operator|.
name|addExtensions
argument_list|(
name|registry
argument_list|,
name|parentType
argument_list|,
name|elementType
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"EXTENSION_ADD_FAILED_MSG"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"EXTENSION_ADD_FAILED_MSG"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|ExtensionRegistry
name|getExtenstionRegistry
parameter_list|()
block|{
return|return
name|registry
return|;
block|}
specifier|public
name|WSDLFactory
name|getWSDLFactory
parameter_list|()
block|{
return|return
name|wsdlFactory
return|;
block|}
specifier|public
name|WSDLReader
name|getWSDLReader
parameter_list|()
block|{
return|return
name|wsdlReader
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|WSDLExtensibilityPlugin
argument_list|>
name|getWSDLPlugins
parameter_list|()
block|{
return|return
name|wsdlPlugins
return|;
block|}
specifier|private
name|void
name|registerWSDLExtensibilityPlugins
parameter_list|()
block|{
name|Properties
name|initialExtensions
init|=
literal|null
decl_stmt|;
try|try
block|{
name|initialExtensions
operator|=
name|PropertiesLoaderUtils
operator|.
name|loadAllProperties
argument_list|(
name|WSDL_PLUGIN_RESOURCE
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
for|for
control|(
name|Iterator
name|it
init|=
name|initialExtensions
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|String
name|key
init|=
operator|(
name|String
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|pluginClz
init|=
name|initialExtensions
operator|.
name|getProperty
argument_list|(
name|key
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Registering : "
operator|+
name|pluginClz
operator|+
literal|" for type: "
operator|+
name|key
argument_list|)
expr_stmt|;
block|}
name|WSDLExtensibilityPlugin
name|plugin
init|=
operator|(
name|WSDLExtensibilityPlugin
operator|)
name|Class
operator|.
name|forName
argument_list|(
name|pluginClz
argument_list|)
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|plugin
operator|.
name|setExtensionRegistry
argument_list|(
name|registry
argument_list|)
expr_stmt|;
name|wsdlPlugins
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|plugin
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"EXTENSION_ADD_FAILED_MSG"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

