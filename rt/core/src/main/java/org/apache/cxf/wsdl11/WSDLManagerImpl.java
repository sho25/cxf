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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|annotation
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|BindingInput
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
name|Types
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
name|ExtensibilityElement
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
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
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
name|xml
operator|.
name|sax
operator|.
name|InputSource
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
name|CatalogWSDLLocator
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
name|common
operator|.
name|injection
operator|.
name|NoJSR250Annotations
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
name|CacheMap
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
name|configuration
operator|.
name|ConfiguredBeanLocator
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|staxutils
operator|.
name|StaxUtils
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
name|WSDLExtensionLoader
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
name|WSDLManager
import|;
end_import

begin_comment
comment|/**  * WSDLManagerImpl  *   * @author dkulp  */
end_comment

begin_class
annotation|@
name|NoJSR250Annotations
argument_list|(
name|unlessNull
operator|=
literal|"bus"
argument_list|)
specifier|public
class|class
name|WSDLManagerImpl
implements|implements
name|WSDLManager
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|WSDLManagerImpl
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
literal|"META-INF/cxf/extensions.xml"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|EXTENSIONS_RESOURCE_COMPAT
init|=
literal|"META-INF/extensions.xml"
decl_stmt|;
specifier|final
name|ExtensionRegistry
name|registry
decl_stmt|;
specifier|final
name|WSDLFactory
name|factory
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|Object
argument_list|,
name|Definition
argument_list|>
name|definitionsMap
decl_stmt|;
comment|/**      * The schemaCacheMap is used as a cache of SchemaInfo against the WSDLDefinitions.      * The key is the same key that is used to hold the definition object into the definitionsMap       */
specifier|final
name|Map
argument_list|<
name|Object
argument_list|,
name|ServiceSchemaInfo
argument_list|>
name|schemaCacheMap
decl_stmt|;
specifier|private
name|boolean
name|disableSchemaCache
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|public
name|WSDLManagerImpl
parameter_list|()
throws|throws
name|BusException
block|{
try|try
block|{
name|factory
operator|=
name|WSDLFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
name|registry
operator|=
name|factory
operator|.
name|newPopulatedExtensionRegistry
argument_list|()
expr_stmt|;
name|registry
operator|.
name|registerSerializer
argument_list|(
name|Types
operator|.
name|class
argument_list|,
name|WSDLConstants
operator|.
name|QNAME_SCHEMA
argument_list|,
operator|new
name|SchemaSerializer
argument_list|()
argument_list|)
expr_stmt|;
comment|// these will replace whatever may have already been registered
comment|// in these places, but there's no good way to check what was
comment|// there before.
name|QName
name|header
init|=
operator|new
name|QName
argument_list|(
name|WSDLConstants
operator|.
name|NS_SOAP
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
name|header
argument_list|,
name|registry
operator|.
name|queryDeserializer
argument_list|(
name|BindingInput
operator|.
name|class
argument_list|,
name|header
argument_list|)
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
name|header
argument_list|,
name|registry
operator|.
name|querySerializer
argument_list|(
name|BindingInput
operator|.
name|class
argument_list|,
name|header
argument_list|)
argument_list|)
expr_stmt|;
comment|// get the original classname of the SOAPHeader
comment|// implementation that was stored in the registry.
name|Class
argument_list|<
name|?
extends|extends
name|ExtensibilityElement
argument_list|>
name|clazz
init|=
name|registry
operator|.
name|createExtension
argument_list|(
name|BindingInput
operator|.
name|class
argument_list|,
name|header
argument_list|)
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|registry
operator|.
name|mapExtensionTypes
argument_list|(
name|MIMEPart
operator|.
name|class
argument_list|,
name|header
argument_list|,
name|clazz
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSDLException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|BusException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|definitionsMap
operator|=
operator|new
name|CacheMap
argument_list|<
name|Object
argument_list|,
name|Definition
argument_list|>
argument_list|()
expr_stmt|;
name|schemaCacheMap
operator|=
operator|new
name|CacheMap
argument_list|<
name|Object
argument_list|,
name|ServiceSchemaInfo
argument_list|>
argument_list|()
expr_stmt|;
name|registerInitialExtensions
argument_list|()
expr_stmt|;
block|}
specifier|public
name|WSDLManagerImpl
parameter_list|(
name|Bus
name|b
parameter_list|)
throws|throws
name|BusException
block|{
name|this
argument_list|()
expr_stmt|;
name|setBus
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Resource
specifier|public
specifier|final
name|void
name|setBus
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|bus
operator|=
name|b
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|bus
condition|)
block|{
name|bus
operator|.
name|setExtension
argument_list|(
name|this
argument_list|,
name|WSDLManager
operator|.
name|class
argument_list|)
expr_stmt|;
name|ConfiguredBeanLocator
name|loc
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ConfiguredBeanLocator
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|loc
operator|!=
literal|null
condition|)
block|{
name|loc
operator|.
name|getBeansOfType
argument_list|(
name|WSDLExtensionLoader
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|WSDLFactory
name|getWSDLFactory
parameter_list|()
block|{
return|return
name|factory
return|;
block|}
specifier|public
name|Map
argument_list|<
name|Object
argument_list|,
name|Definition
argument_list|>
name|getDefinitions
parameter_list|()
block|{
synchronized|synchronized
init|(
name|definitionsMap
init|)
block|{
return|return
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|definitionsMap
argument_list|)
return|;
block|}
block|}
comment|/*      * (non-Javadoc)      *       * XXX - getExtensionRegistry()      *       * @see org.apache.cxf.wsdl.WSDLManager#getExtenstionRegistry()      */
specifier|public
name|ExtensionRegistry
name|getExtensionRegistry
parameter_list|()
block|{
return|return
name|registry
return|;
block|}
comment|/*      * (non-Javadoc)      *       * @see org.apache.cxf.wsdl.WSDLManager#getDefinition(java.net.URL)      */
specifier|public
name|Definition
name|getDefinition
parameter_list|(
name|URL
name|url
parameter_list|)
throws|throws
name|WSDLException
block|{
synchronized|synchronized
init|(
name|definitionsMap
init|)
block|{
if|if
condition|(
name|definitionsMap
operator|.
name|containsKey
argument_list|(
name|url
argument_list|)
condition|)
block|{
return|return
name|definitionsMap
operator|.
name|get
argument_list|(
name|url
argument_list|)
return|;
block|}
block|}
name|Definition
name|def
init|=
name|loadDefinition
argument_list|(
name|url
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
synchronized|synchronized
init|(
name|definitionsMap
init|)
block|{
name|definitionsMap
operator|.
name|put
argument_list|(
name|url
argument_list|,
name|def
argument_list|)
expr_stmt|;
block|}
return|return
name|def
return|;
block|}
comment|/*      * (non-Javadoc)      *       * @see org.apache.cxf.wsdl.WSDLManager#getDefinition(java.lang.String)      */
specifier|public
name|Definition
name|getDefinition
parameter_list|(
name|String
name|url
parameter_list|)
throws|throws
name|WSDLException
block|{
synchronized|synchronized
init|(
name|definitionsMap
init|)
block|{
if|if
condition|(
name|definitionsMap
operator|.
name|containsKey
argument_list|(
name|url
argument_list|)
condition|)
block|{
return|return
name|definitionsMap
operator|.
name|get
argument_list|(
name|url
argument_list|)
return|;
block|}
block|}
return|return
name|loadDefinition
argument_list|(
name|url
argument_list|)
return|;
block|}
specifier|public
name|Definition
name|getDefinition
parameter_list|(
name|Element
name|el
parameter_list|)
throws|throws
name|WSDLException
block|{
synchronized|synchronized
init|(
name|definitionsMap
init|)
block|{
if|if
condition|(
name|definitionsMap
operator|.
name|containsKey
argument_list|(
name|el
argument_list|)
condition|)
block|{
return|return
name|definitionsMap
operator|.
name|get
argument_list|(
name|el
argument_list|)
return|;
block|}
block|}
name|WSDLReader
name|reader
init|=
name|factory
operator|.
name|newWSDLReader
argument_list|()
decl_stmt|;
name|reader
operator|.
name|setFeature
argument_list|(
literal|"javax.wsdl.verbose"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|reader
operator|.
name|setExtensionRegistry
argument_list|(
name|registry
argument_list|)
expr_stmt|;
name|Definition
name|def
init|=
name|reader
operator|.
name|readWSDL
argument_list|(
literal|""
argument_list|,
name|el
argument_list|)
decl_stmt|;
synchronized|synchronized
init|(
name|definitionsMap
init|)
block|{
name|definitionsMap
operator|.
name|put
argument_list|(
name|el
argument_list|,
name|def
argument_list|)
expr_stmt|;
block|}
return|return
name|def
return|;
block|}
specifier|public
name|void
name|addDefinition
parameter_list|(
name|Object
name|key
parameter_list|,
name|Definition
name|wsdl
parameter_list|)
block|{
synchronized|synchronized
init|(
name|definitionsMap
init|)
block|{
name|definitionsMap
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|Definition
name|loadDefinition
parameter_list|(
name|String
name|url
parameter_list|)
throws|throws
name|WSDLException
block|{
name|WSDLReader
name|reader
init|=
name|factory
operator|.
name|newWSDLReader
argument_list|()
decl_stmt|;
name|reader
operator|.
name|setFeature
argument_list|(
literal|"javax.wsdl.verbose"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|reader
operator|.
name|setFeature
argument_list|(
literal|"javax.wsdl.importDocuments"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|reader
operator|.
name|setExtensionRegistry
argument_list|(
name|registry
argument_list|)
expr_stmt|;
name|CatalogWSDLLocator
name|catLocator
init|=
operator|new
name|CatalogWSDLLocator
argument_list|(
name|url
argument_list|,
name|bus
argument_list|)
decl_stmt|;
name|ResourceManagerWSDLLocator
name|wsdlLocator
init|=
operator|new
name|ResourceManagerWSDLLocator
argument_list|(
name|url
argument_list|,
name|catLocator
argument_list|,
name|bus
argument_list|)
decl_stmt|;
name|InputSource
name|src
init|=
name|wsdlLocator
operator|.
name|getBaseInputSource
argument_list|()
decl_stmt|;
name|Definition
name|def
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|src
operator|.
name|getByteStream
argument_list|()
operator|!=
literal|null
operator|||
name|src
operator|.
name|getCharacterStream
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Document
name|doc
decl_stmt|;
try|try
block|{
name|doc
operator|=
name|StaxUtils
operator|.
name|read
argument_list|(
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|src
argument_list|)
argument_list|,
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|src
operator|.
name|getSystemId
argument_list|()
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|doc
operator|.
name|setDocumentURI
argument_list|(
operator|new
name|String
argument_list|(
name|src
operator|.
name|getSystemId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//ignore - probably not DOM level 3
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|WSDLException
argument_list|(
name|WSDLException
operator|.
name|PARSER_ERROR
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|def
operator|=
name|reader
operator|.
name|readWSDL
argument_list|(
name|wsdlLocator
argument_list|,
name|doc
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|def
operator|=
name|reader
operator|.
name|readWSDL
argument_list|(
name|wsdlLocator
argument_list|)
expr_stmt|;
block|}
synchronized|synchronized
init|(
name|definitionsMap
init|)
block|{
name|definitionsMap
operator|.
name|put
argument_list|(
name|url
argument_list|,
name|def
argument_list|)
expr_stmt|;
block|}
return|return
name|def
return|;
block|}
specifier|private
name|void
name|registerInitialExtensions
parameter_list|()
throws|throws
name|BusException
block|{
name|registerInitialXmlExtensions
argument_list|(
name|EXTENSIONS_RESOURCE_COMPAT
argument_list|)
expr_stmt|;
name|registerInitialXmlExtensions
argument_list|(
name|EXTENSIONS_RESOURCE
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|registerInitialXmlExtensions
parameter_list|(
name|String
name|resource
parameter_list|)
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
name|resource
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
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
argument_list|<
name|?
argument_list|>
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
name|ServiceSchemaInfo
name|getSchemasForDefinition
parameter_list|(
name|Definition
name|wsdl
parameter_list|)
block|{
if|if
condition|(
name|disableSchemaCache
condition|)
block|{
return|return
literal|null
return|;
block|}
synchronized|synchronized
init|(
name|definitionsMap
init|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Object
argument_list|,
name|Definition
argument_list|>
name|e
range|:
name|definitionsMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|e
operator|.
name|getValue
argument_list|()
operator|==
name|wsdl
condition|)
block|{
name|ServiceSchemaInfo
name|info
init|=
name|schemaCacheMap
operator|.
name|get
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|info
operator|!=
literal|null
condition|)
block|{
return|return
name|info
return|;
block|}
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|putSchemasForDefinition
parameter_list|(
name|Definition
name|wsdl
parameter_list|,
name|ServiceSchemaInfo
name|schemas
parameter_list|)
block|{
if|if
condition|(
operator|!
name|disableSchemaCache
condition|)
block|{
synchronized|synchronized
init|(
name|definitionsMap
init|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Object
argument_list|,
name|Definition
argument_list|>
name|e
range|:
name|definitionsMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|e
operator|.
name|getValue
argument_list|()
operator|==
name|wsdl
condition|)
block|{
name|schemaCacheMap
operator|.
name|put
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|schemas
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
specifier|public
name|boolean
name|isDisableSchemaCache
parameter_list|()
block|{
return|return
name|disableSchemaCache
return|;
block|}
comment|/**      * There's a test that 'fails' by succeeding if the cache is operational.      * @param disableSchemaCache      */
specifier|public
name|void
name|setDisableSchemaCache
parameter_list|(
name|boolean
name|disableSchemaCache
parameter_list|)
block|{
name|this
operator|.
name|disableSchemaCache
operator|=
name|disableSchemaCache
expr_stmt|;
block|}
specifier|public
name|void
name|removeDefinition
parameter_list|(
name|Definition
name|wsdl
parameter_list|)
block|{
synchronized|synchronized
init|(
name|definitionsMap
init|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|keys
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Object
argument_list|,
name|Definition
argument_list|>
name|e
range|:
name|definitionsMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|e
operator|.
name|getValue
argument_list|()
operator|==
name|wsdl
condition|)
block|{
name|keys
operator|.
name|add
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|Object
name|o
range|:
name|keys
control|)
block|{
name|definitionsMap
operator|.
name|remove
argument_list|(
name|o
argument_list|)
expr_stmt|;
name|schemaCacheMap
operator|.
name|remove
argument_list|(
name|o
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

