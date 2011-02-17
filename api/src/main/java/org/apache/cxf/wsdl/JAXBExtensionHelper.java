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
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|Map
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
name|ExtensionDeserializer
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
name|ExtensionSerializer
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
name|JAXBContext
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
name|JAXBElement
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
name|bind
operator|.
name|Marshaller
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
name|Unmarshaller
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
name|annotation
operator|.
name|XmlElementDecl
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
name|annotation
operator|.
name|XmlRootElement
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
name|annotation
operator|.
name|XmlSchema
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
name|NamespaceContext
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
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamWriter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|util
operator|.
name|StreamReaderDelegate
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
name|common
operator|.
name|classloader
operator|.
name|ClassLoaderUtils
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
name|PackageUtils
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
name|StringUtils
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

begin_comment
comment|/**  * JAXBExtensionHelper  * @author dkulp  *  */
end_comment

begin_class
specifier|public
class|class
name|JAXBExtensionHelper
implements|implements
name|ExtensionSerializer
implements|,
name|ExtensionDeserializer
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
name|JAXBExtensionHelper
operator|.
name|class
argument_list|)
decl_stmt|;
name|JAXBContext
name|context
decl_stmt|;
specifier|final
name|Class
argument_list|<
name|?
extends|extends
name|ExtensibilityElement
argument_list|>
name|typeClass
decl_stmt|;
specifier|final
name|String
name|namespace
decl_stmt|;
name|String
name|jaxbNamespace
decl_stmt|;
specifier|public
name|JAXBExtensionHelper
parameter_list|(
name|Class
argument_list|<
name|?
extends|extends
name|ExtensibilityElement
argument_list|>
name|cls
parameter_list|,
name|String
name|ns
parameter_list|)
block|{
name|typeClass
operator|=
name|cls
expr_stmt|;
name|namespace
operator|=
name|ns
expr_stmt|;
block|}
name|void
name|setJaxbNamespace
parameter_list|(
name|String
name|ns
parameter_list|)
block|{
name|jaxbNamespace
operator|=
name|ns
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|addExtensions
parameter_list|(
name|ExtensionRegistry
name|registry
parameter_list|,
name|String
name|parentType
parameter_list|,
name|String
name|elementType
parameter_list|)
throws|throws
name|JAXBException
throws|,
name|ClassNotFoundException
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|parentTypeClass
init|=
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|parentType
argument_list|,
name|JAXBExtensionHelper
operator|.
name|class
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
extends|extends
name|ExtensibilityElement
argument_list|>
name|elementTypeClass
init|=
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|elementType
argument_list|,
name|JAXBExtensionHelper
operator|.
name|class
argument_list|)
operator|.
name|asSubclass
argument_list|(
name|ExtensibilityElement
operator|.
name|class
argument_list|)
decl_stmt|;
name|addExtensions
argument_list|(
name|registry
argument_list|,
name|parentTypeClass
argument_list|,
name|elementTypeClass
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|addExtensions
parameter_list|(
name|ExtensionRegistry
name|registry
parameter_list|,
name|String
name|parentType
parameter_list|,
name|String
name|elementType
parameter_list|,
name|String
name|namespace
parameter_list|)
throws|throws
name|JAXBException
throws|,
name|ClassNotFoundException
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|parentTypeClass
init|=
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|parentType
argument_list|,
name|JAXBExtensionHelper
operator|.
name|class
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
extends|extends
name|ExtensibilityElement
argument_list|>
name|elementTypeClass
init|=
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
name|elementType
argument_list|,
name|JAXBExtensionHelper
operator|.
name|class
argument_list|)
operator|.
name|asSubclass
argument_list|(
name|ExtensibilityElement
operator|.
name|class
argument_list|)
decl_stmt|;
name|addExtensions
argument_list|(
name|registry
argument_list|,
name|parentTypeClass
argument_list|,
name|elementTypeClass
argument_list|,
name|namespace
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|addExtensions
parameter_list|(
name|ExtensionRegistry
name|registry
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|parentType
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|ExtensibilityElement
argument_list|>
name|cls
parameter_list|)
throws|throws
name|JAXBException
block|{
name|addExtensions
argument_list|(
name|registry
argument_list|,
name|parentType
argument_list|,
name|cls
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|addExtensions
parameter_list|(
name|ExtensionRegistry
name|registry
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|parentType
parameter_list|,
name|Class
argument_list|<
name|?
extends|extends
name|ExtensibilityElement
argument_list|>
name|cls
parameter_list|,
name|String
name|namespace
parameter_list|)
throws|throws
name|JAXBException
block|{
name|JAXBExtensionHelper
name|helper
init|=
operator|new
name|JAXBExtensionHelper
argument_list|(
name|cls
argument_list|,
name|namespace
argument_list|)
decl_stmt|;
name|boolean
name|found
init|=
literal|false
decl_stmt|;
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|objectFactory
init|=
name|Class
operator|.
name|forName
argument_list|(
name|PackageUtils
operator|.
name|getPackageName
argument_list|(
name|cls
argument_list|)
operator|+
literal|".ObjectFactory"
argument_list|,
literal|true
argument_list|,
name|cls
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
name|Method
name|methods
index|[]
init|=
name|objectFactory
operator|.
name|getDeclaredMethods
argument_list|()
decl_stmt|;
for|for
control|(
name|Method
name|method
range|:
name|methods
control|)
block|{
if|if
condition|(
name|method
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
operator|==
literal|1
operator|&&
name|method
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|0
index|]
operator|.
name|equals
argument_list|(
name|cls
argument_list|)
condition|)
block|{
name|XmlElementDecl
name|elementDecl
init|=
name|method
operator|.
name|getAnnotation
argument_list|(
name|XmlElementDecl
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|elementDecl
condition|)
block|{
name|String
name|name
init|=
name|elementDecl
operator|.
name|name
argument_list|()
decl_stmt|;
name|String
name|ns
init|=
name|namespace
operator|!=
literal|null
condition|?
name|namespace
else|:
name|elementDecl
operator|.
name|namespace
argument_list|()
decl_stmt|;
if|if
condition|(
name|namespace
operator|!=
literal|null
condition|)
block|{
name|helper
operator|.
name|setJaxbNamespace
argument_list|(
name|elementDecl
operator|.
name|namespace
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|QName
name|elementType
init|=
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|registry
operator|.
name|registerDeserializer
argument_list|(
name|parentType
argument_list|,
name|elementType
argument_list|,
name|helper
argument_list|)
expr_stmt|;
name|registry
operator|.
name|registerSerializer
argument_list|(
name|parentType
argument_list|,
name|elementType
argument_list|,
name|helper
argument_list|)
expr_stmt|;
name|registry
operator|.
name|mapExtensionTypes
argument_list|(
name|parentType
argument_list|,
name|elementType
argument_list|,
name|cls
argument_list|)
expr_stmt|;
name|found
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|ex
parameter_list|)
block|{
comment|//ignore
block|}
if|if
condition|(
operator|!
name|found
condition|)
block|{
comment|//not in object factory or no object factory, try other annotations
name|XmlRootElement
name|elAnnot
init|=
name|cls
operator|.
name|getAnnotation
argument_list|(
name|XmlRootElement
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|elAnnot
operator|!=
literal|null
condition|)
block|{
name|String
name|name
init|=
name|elAnnot
operator|.
name|name
argument_list|()
decl_stmt|;
name|String
name|ns
init|=
name|elAnnot
operator|.
name|namespace
argument_list|()
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|ns
argument_list|)
operator|||
literal|"##default"
operator|.
name|equals
argument_list|(
name|ns
argument_list|)
condition|)
block|{
name|XmlSchema
name|schema
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|cls
operator|.
name|getPackage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|schema
operator|=
name|cls
operator|.
name|getPackage
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|XmlSchema
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|schema
operator|!=
literal|null
condition|)
block|{
name|ns
operator|=
name|schema
operator|.
name|namespace
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|ns
argument_list|)
operator|&&
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|name
argument_list|)
condition|)
block|{
if|if
condition|(
name|namespace
operator|!=
literal|null
condition|)
block|{
name|helper
operator|.
name|setJaxbNamespace
argument_list|(
name|ns
argument_list|)
expr_stmt|;
name|ns
operator|=
name|namespace
expr_stmt|;
block|}
name|QName
name|elementType
init|=
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
name|name
argument_list|)
decl_stmt|;
name|registry
operator|.
name|registerDeserializer
argument_list|(
name|parentType
argument_list|,
name|elementType
argument_list|,
name|helper
argument_list|)
expr_stmt|;
name|registry
operator|.
name|registerSerializer
argument_list|(
name|parentType
argument_list|,
name|elementType
argument_list|,
name|helper
argument_list|)
expr_stmt|;
name|registry
operator|.
name|mapExtensionTypes
argument_list|(
name|parentType
argument_list|,
name|elementType
argument_list|,
name|cls
argument_list|)
expr_stmt|;
name|found
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|found
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"EXTENSION_NOT_REGISTERED"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|cls
operator|.
name|getName
argument_list|()
block|,
name|parentType
operator|.
name|getName
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|JAXBContext
name|getJAXBContext
parameter_list|()
block|{
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|createJAXBContext
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JAXBException
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
return|return
name|context
return|;
block|}
specifier|protected
specifier|synchronized
name|void
name|createJAXBContext
parameter_list|()
throws|throws
name|JAXBException
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
return|return;
block|}
name|context
operator|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|PackageUtils
operator|.
name|getPackageName
argument_list|(
name|typeClass
argument_list|)
argument_list|,
name|typeClass
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/* (non-Javadoc)      * @see javax.wsdl.extensions.ExtensionSerializer#marshall(java.lang.Class,      *  javax.xml.namespace.QName, javax.wsdl.extensions.ExtensibilityElement,      *   java.io.PrintWriter, javax.wsdl.Definition, javax.wsdl.extensions.ExtensionRegistry)      */
specifier|public
name|void
name|marshall
parameter_list|(
name|Class
name|parent
parameter_list|,
name|QName
name|qname
parameter_list|,
name|ExtensibilityElement
name|obj
parameter_list|,
name|PrintWriter
name|pw
parameter_list|,
specifier|final
name|Definition
name|wsdl
parameter_list|,
name|ExtensionRegistry
name|registry
parameter_list|)
throws|throws
name|WSDLException
block|{
comment|// TODO Auto-generated method stub
try|try
block|{
name|Marshaller
name|u
init|=
name|getJAXBContext
argument_list|()
operator|.
name|createMarshaller
argument_list|()
decl_stmt|;
name|u
operator|.
name|setProperty
argument_list|(
literal|"jaxb.encoding"
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|u
operator|.
name|setProperty
argument_list|(
literal|"jaxb.fragment"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|u
operator|.
name|setProperty
argument_list|(
literal|"jaxb.formatted.output"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|Object
name|mObj
init|=
name|obj
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|objectFactory
init|=
name|Class
operator|.
name|forName
argument_list|(
name|PackageUtils
operator|.
name|getPackageName
argument_list|(
name|typeClass
argument_list|)
operator|+
literal|".ObjectFactory"
argument_list|,
literal|true
argument_list|,
name|obj
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
name|Method
name|methods
index|[]
init|=
name|objectFactory
operator|.
name|getDeclaredMethods
argument_list|()
decl_stmt|;
for|for
control|(
name|Method
name|method
range|:
name|methods
control|)
block|{
if|if
condition|(
name|method
operator|.
name|getParameterTypes
argument_list|()
operator|.
name|length
operator|==
literal|1
operator|&&
name|method
operator|.
name|getParameterTypes
argument_list|()
index|[
literal|0
index|]
operator|.
name|equals
argument_list|(
name|typeClass
argument_list|)
condition|)
block|{
name|mObj
operator|=
name|method
operator|.
name|invoke
argument_list|(
name|objectFactory
operator|.
name|newInstance
argument_list|()
argument_list|,
operator|new
name|Object
index|[]
block|{
name|obj
block|}
argument_list|)
expr_stmt|;
block|}
block|}
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLOutputFactory
name|fact
init|=
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLOutputFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|XMLStreamWriter
name|writer
init|=
operator|new
name|PrettyPrintXMLStreamWriter
argument_list|(
name|fact
operator|.
name|createXMLStreamWriter
argument_list|(
name|pw
argument_list|)
argument_list|,
name|parent
argument_list|)
decl_stmt|;
name|writer
operator|.
name|setNamespaceContext
argument_list|(
operator|new
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|NamespaceContext
argument_list|()
block|{
specifier|public
name|String
name|getNamespaceURI
parameter_list|(
name|String
name|arg
parameter_list|)
block|{
return|return
name|wsdl
operator|.
name|getNamespace
argument_list|(
name|arg
argument_list|)
return|;
block|}
specifier|public
name|String
name|getPrefix
parameter_list|(
name|String
name|arg
parameter_list|)
block|{
if|if
condition|(
name|arg
operator|.
name|equals
argument_list|(
name|jaxbNamespace
argument_list|)
condition|)
block|{
name|arg
operator|=
name|namespace
expr_stmt|;
block|}
for|for
control|(
name|Object
name|ent
range|:
name|wsdl
operator|.
name|getNamespaces
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Map
operator|.
name|Entry
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|ent
decl_stmt|;
if|if
condition|(
name|arg
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
return|return
operator|(
name|String
operator|)
name|entry
operator|.
name|getKey
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|Iterator
name|getPrefixes
parameter_list|(
name|String
name|arg
parameter_list|)
block|{
if|if
condition|(
name|arg
operator|.
name|equals
argument_list|(
name|jaxbNamespace
argument_list|)
condition|)
block|{
name|arg
operator|=
name|namespace
expr_stmt|;
block|}
return|return
name|wsdl
operator|.
name|getNamespaces
argument_list|()
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|u
operator|.
name|marshal
argument_list|(
name|mObj
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
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
literal|""
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
comment|/* (non-Javadoc)      * @see javax.wsdl.extensions.ExtensionDeserializer#unmarshall(java.lang.Class,      *  javax.xml.namespace.QName, org.w3c.dom.Element,      *   javax.wsdl.Definition,      *   javax.wsdl.extensions.ExtensionRegistry)      */
specifier|public
name|ExtensibilityElement
name|unmarshall
parameter_list|(
name|Class
name|parent
parameter_list|,
name|QName
name|qname
parameter_list|,
name|Element
name|element
parameter_list|,
name|Definition
name|wsdl
parameter_list|,
name|ExtensionRegistry
name|registry
parameter_list|)
throws|throws
name|WSDLException
block|{
try|try
block|{
name|Unmarshaller
name|u
init|=
name|getJAXBContext
argument_list|()
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
name|Object
name|o
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|namespace
operator|==
literal|null
condition|)
block|{
name|o
operator|=
name|u
operator|.
name|unmarshal
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|element
argument_list|)
decl_stmt|;
name|reader
operator|=
operator|new
name|MappingReaderDelegate
argument_list|(
name|reader
argument_list|)
expr_stmt|;
name|o
operator|=
name|u
operator|.
name|unmarshal
argument_list|(
name|reader
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|o
operator|instanceof
name|JAXBElement
argument_list|<
name|?
argument_list|>
condition|)
block|{
name|JAXBElement
argument_list|<
name|?
argument_list|>
name|el
init|=
operator|(
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|)
name|o
decl_stmt|;
name|o
operator|=
name|el
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
name|ExtensibilityElement
name|el
init|=
name|o
operator|instanceof
name|ExtensibilityElement
condition|?
operator|(
name|ExtensibilityElement
operator|)
name|o
else|:
literal|null
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|el
condition|)
block|{
name|el
operator|.
name|setElementType
argument_list|(
name|qname
argument_list|)
expr_stmt|;
block|}
return|return
name|el
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
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
literal|"Error reading element "
operator|+
name|qname
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
class|class
name|MappingReaderDelegate
extends|extends
name|StreamReaderDelegate
block|{
name|MappingReaderDelegate
parameter_list|(
name|XMLStreamReader
name|reader
parameter_list|)
block|{
name|super
argument_list|(
name|reader
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|NamespaceContext
name|getNamespaceContext
parameter_list|()
block|{
specifier|final
name|NamespaceContext
name|ctx
init|=
name|super
operator|.
name|getNamespaceContext
argument_list|()
decl_stmt|;
return|return
operator|new
name|NamespaceContext
argument_list|()
block|{
specifier|public
name|String
name|getNamespaceURI
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
name|String
name|ns
init|=
name|ctx
operator|.
name|getNamespaceURI
argument_list|(
name|prefix
argument_list|)
decl_stmt|;
if|if
condition|(
name|namespace
operator|.
name|equals
argument_list|(
name|ns
argument_list|)
condition|)
block|{
name|ns
operator|=
name|jaxbNamespace
expr_stmt|;
block|}
return|return
name|ns
return|;
block|}
specifier|public
name|String
name|getPrefix
parameter_list|(
name|String
name|namespaceURI
parameter_list|)
block|{
if|if
condition|(
name|jaxbNamespace
operator|.
name|equals
argument_list|(
name|namespaceURI
argument_list|)
condition|)
block|{
return|return
name|ctx
operator|.
name|getPrefix
argument_list|(
name|namespace
argument_list|)
return|;
block|}
return|return
name|ctx
operator|.
name|getPrefix
argument_list|(
name|namespaceURI
argument_list|)
return|;
block|}
specifier|public
name|Iterator
name|getPrefixes
parameter_list|(
name|String
name|namespaceURI
parameter_list|)
block|{
if|if
condition|(
name|jaxbNamespace
operator|.
name|equals
argument_list|(
name|namespaceURI
argument_list|)
condition|)
block|{
return|return
name|ctx
operator|.
name|getPrefixes
argument_list|(
name|namespace
argument_list|)
return|;
block|}
return|return
name|ctx
operator|.
name|getPrefixes
argument_list|(
name|namespaceURI
argument_list|)
return|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getNamespaceURI
parameter_list|(
name|int
name|index
parameter_list|)
block|{
name|String
name|ns
init|=
name|super
operator|.
name|getNamespaceURI
argument_list|(
name|index
argument_list|)
decl_stmt|;
if|if
condition|(
name|namespace
operator|.
name|equals
argument_list|(
name|ns
argument_list|)
condition|)
block|{
name|ns
operator|=
name|jaxbNamespace
expr_stmt|;
block|}
return|return
name|ns
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getNamespaceURI
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
name|String
name|ns
init|=
name|super
operator|.
name|getNamespaceURI
argument_list|(
name|prefix
argument_list|)
decl_stmt|;
if|if
condition|(
name|namespace
operator|.
name|equals
argument_list|(
name|ns
argument_list|)
condition|)
block|{
name|ns
operator|=
name|jaxbNamespace
expr_stmt|;
block|}
return|return
name|ns
return|;
block|}
annotation|@
name|Override
specifier|public
name|QName
name|getName
parameter_list|()
block|{
name|QName
name|qn
init|=
name|super
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|namespace
operator|.
name|equals
argument_list|(
name|qn
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
name|qn
operator|=
operator|new
name|QName
argument_list|(
name|jaxbNamespace
argument_list|,
name|qn
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|qn
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getNamespaceURI
parameter_list|()
block|{
name|String
name|ns
init|=
name|super
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
if|if
condition|(
name|namespace
operator|.
name|equals
argument_list|(
name|ns
argument_list|)
condition|)
block|{
name|ns
operator|=
name|jaxbNamespace
expr_stmt|;
block|}
return|return
name|ns
return|;
block|}
block|}
empty_stmt|;
block|}
end_class

end_unit

