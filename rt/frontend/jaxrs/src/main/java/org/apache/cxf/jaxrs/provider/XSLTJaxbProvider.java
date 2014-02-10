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
name|jaxrs
operator|.
name|provider
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
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
name|Type
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
name|ws
operator|.
name|rs
operator|.
name|Consumes
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Produces
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MultivaluedMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|PathSegment
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|UriInfo
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|Provider
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
name|transform
operator|.
name|Result
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Source
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Templates
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|Transformer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerConfigurationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|URIResolver
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|sax
operator|.
name|SAXSource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|sax
operator|.
name|SAXTransformerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|sax
operator|.
name|TransformerHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamResult
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
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
name|xml
operator|.
name|sax
operator|.
name|XMLFilter
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
name|io
operator|.
name|CachedOutputStream
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
name|jaxrs
operator|.
name|ext
operator|.
name|MessageContext
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
name|jaxrs
operator|.
name|utils
operator|.
name|ExceptionUtils
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
name|jaxrs
operator|.
name|utils
operator|.
name|InjectionUtils
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
name|jaxrs
operator|.
name|utils
operator|.
name|ResourceUtils
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
name|StaxSource
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

begin_class
annotation|@
name|Produces
argument_list|(
block|{
literal|"application/xml"
block|,
literal|"application/*+xml"
block|,
literal|"text/xml"
block|,
literal|"text/html"
block|}
argument_list|)
annotation|@
name|Consumes
argument_list|(
block|{
literal|"application/xml"
block|,
literal|"application/*+xml"
block|,
literal|"text/xml"
block|,
literal|"text/html"
block|}
argument_list|)
annotation|@
name|Provider
specifier|public
class|class
name|XSLTJaxbProvider
parameter_list|<
name|T
parameter_list|>
extends|extends
name|JAXBElementProvider
argument_list|<
name|T
argument_list|>
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
name|XSLTJaxbProvider
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ABSOLUTE_PATH_PARAMETER
init|=
literal|"absolute.path"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|BASE_PATH_PARAMETER
init|=
literal|"base.path"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RELATIVE_PATH_PARAMETER
init|=
literal|"relative.path"
decl_stmt|;
specifier|private
name|SAXTransformerFactory
name|factory
decl_stmt|;
specifier|private
name|Templates
name|inTemplates
decl_stmt|;
specifier|private
name|Templates
name|outTemplates
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Templates
argument_list|>
name|inMediaTemplates
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Templates
argument_list|>
name|outMediaTemplates
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|inClassesToHandle
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|outClassesToHandle
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|inParamsMap
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|outParamsMap
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|inProperties
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|outProperties
decl_stmt|;
specifier|private
name|URIResolver
name|uriResolver
decl_stmt|;
specifier|private
name|String
name|systemId
decl_stmt|;
specifier|private
name|boolean
name|supportJaxbOnly
decl_stmt|;
specifier|public
name|void
name|setSupportJaxbOnly
parameter_list|(
name|boolean
name|support
parameter_list|)
block|{
name|this
operator|.
name|supportJaxbOnly
operator|=
name|support
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isReadable
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|Annotation
index|[]
name|anns
parameter_list|,
name|MediaType
name|mt
parameter_list|)
block|{
if|if
condition|(
operator|!
name|super
operator|.
name|isReadable
argument_list|(
name|type
argument_list|,
name|genericType
argument_list|,
name|anns
argument_list|,
name|mt
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|InjectionUtils
operator|.
name|isSupportedCollectionOrArray
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
name|supportJaxbOnly
return|;
block|}
comment|// if the user has set the list of in classes and a given class
comment|// is in that list then it can only be handled by the template
if|if
condition|(
name|inClassCanBeHandled
argument_list|(
name|type
operator|.
name|getName
argument_list|()
argument_list|)
operator|||
name|inClassesToHandle
operator|==
literal|null
operator|&&
operator|!
name|supportJaxbOnly
condition|)
block|{
return|return
name|inTemplatesAvailable
argument_list|(
name|mt
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|supportJaxbOnly
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isWriteable
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|Annotation
index|[]
name|anns
parameter_list|,
name|MediaType
name|mt
parameter_list|)
block|{
comment|// JAXB support is required
if|if
condition|(
operator|!
name|super
operator|.
name|isWriteable
argument_list|(
name|type
argument_list|,
name|genericType
argument_list|,
name|anns
argument_list|,
name|mt
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|InjectionUtils
operator|.
name|isSupportedCollectionOrArray
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
name|supportJaxbOnly
return|;
block|}
comment|// if the user has set the list of out classes and a given class
comment|// is in that list then it can only be handled by the template
if|if
condition|(
name|outClassCanBeHandled
argument_list|(
name|type
operator|.
name|getName
argument_list|()
argument_list|)
operator|||
name|outClassesToHandle
operator|==
literal|null
operator|&&
operator|!
name|supportJaxbOnly
condition|)
block|{
return|return
name|outTemplatesAvailable
argument_list|(
name|mt
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|supportJaxbOnly
return|;
block|}
block|}
specifier|protected
name|boolean
name|inTemplatesAvailable
parameter_list|(
name|MediaType
name|mt
parameter_list|)
block|{
return|return
name|inTemplates
operator|!=
literal|null
operator|||
name|inMediaTemplates
operator|!=
literal|null
operator|&&
name|inMediaTemplates
operator|.
name|containsKey
argument_list|(
name|mt
operator|.
name|getType
argument_list|()
operator|+
literal|"/"
operator|+
name|mt
operator|.
name|getSubtype
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|boolean
name|outTemplatesAvailable
parameter_list|(
name|MediaType
name|mt
parameter_list|)
block|{
return|return
name|outTemplates
operator|!=
literal|null
operator|||
name|outMediaTemplates
operator|!=
literal|null
operator|&&
name|outMediaTemplates
operator|.
name|containsKey
argument_list|(
name|mt
operator|.
name|getType
argument_list|()
operator|+
literal|"/"
operator|+
name|mt
operator|.
name|getSubtype
argument_list|()
argument_list|)
return|;
block|}
specifier|protected
name|Templates
name|getInTemplates
parameter_list|(
name|MediaType
name|mt
parameter_list|)
block|{
return|return
name|inTemplates
operator|!=
literal|null
condition|?
name|inTemplates
else|:
name|inMediaTemplates
operator|!=
literal|null
condition|?
name|inMediaTemplates
operator|.
name|get
argument_list|(
name|mt
operator|.
name|getType
argument_list|()
operator|+
literal|"/"
operator|+
name|mt
operator|.
name|getSubtype
argument_list|()
argument_list|)
else|:
literal|null
return|;
block|}
specifier|protected
name|Templates
name|getOutTemplates
parameter_list|(
name|MediaType
name|mt
parameter_list|)
block|{
return|return
name|outTemplates
operator|!=
literal|null
condition|?
name|outTemplates
else|:
name|outMediaTemplates
operator|!=
literal|null
condition|?
name|outMediaTemplates
operator|.
name|get
argument_list|(
name|mt
operator|.
name|getType
argument_list|()
operator|+
literal|"/"
operator|+
name|mt
operator|.
name|getSubtype
argument_list|()
argument_list|)
else|:
literal|null
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|unmarshalFromInputStream
parameter_list|(
name|Unmarshaller
name|unmarshaller
parameter_list|,
name|InputStream
name|is
parameter_list|,
name|MediaType
name|mt
parameter_list|)
throws|throws
name|JAXBException
block|{
try|try
block|{
name|Templates
name|t
init|=
name|createTemplates
argument_list|(
name|getInTemplates
argument_list|(
name|mt
argument_list|)
argument_list|,
name|inParamsMap
argument_list|,
name|inProperties
argument_list|)
decl_stmt|;
if|if
condition|(
name|t
operator|==
literal|null
operator|&&
name|supportJaxbOnly
condition|)
block|{
return|return
name|super
operator|.
name|unmarshalFromInputStream
argument_list|(
name|unmarshaller
argument_list|,
name|is
argument_list|,
name|mt
argument_list|)
return|;
block|}
name|XMLFilter
name|filter
init|=
literal|null
decl_stmt|;
try|try
block|{
name|filter
operator|=
name|factory
operator|.
name|newXMLFilter
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TransformerConfigurationException
name|ex
parameter_list|)
block|{
name|TemplatesImpl
name|ti
init|=
operator|(
name|TemplatesImpl
operator|)
name|t
decl_stmt|;
name|filter
operator|=
name|factory
operator|.
name|newXMLFilter
argument_list|(
name|ti
operator|.
name|getTemplates
argument_list|()
argument_list|)
expr_stmt|;
name|trySettingProperties
argument_list|(
name|filter
argument_list|,
name|ti
argument_list|)
expr_stmt|;
block|}
name|SAXSource
name|source
init|=
operator|new
name|SAXSource
argument_list|(
name|filter
argument_list|,
operator|new
name|InputSource
argument_list|(
name|is
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|systemId
operator|!=
literal|null
condition|)
block|{
name|source
operator|.
name|setSystemId
argument_list|(
name|systemId
argument_list|)
expr_stmt|;
block|}
return|return
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
name|source
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|TransformerConfigurationException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Transformation exception : "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|ExceptionUtils
operator|.
name|toInternalServerErrorException
argument_list|(
name|ex
argument_list|,
literal|null
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|trySettingProperties
parameter_list|(
name|Object
name|filter
parameter_list|,
name|TemplatesImpl
name|ti
parameter_list|)
block|{
try|try
block|{
comment|//Saxon doesn't allow creating a Filter or Handler from anything other than it's original
comment|//Templates.  That then requires setting the parameters after the fact, but there
comment|//isn't a standard API for that, so we have to grab the Transformer via reflection to
comment|//set the parameters.
name|Transformer
name|tr
init|=
operator|(
name|Transformer
operator|)
name|filter
operator|.
name|getClass
argument_list|()
operator|.
name|getMethod
argument_list|(
literal|"getTransformer"
argument_list|)
operator|.
name|invoke
argument_list|(
name|filter
argument_list|)
decl_stmt|;
name|tr
operator|.
name|setURIResolver
argument_list|(
name|ti
operator|.
name|resolver
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|ti
operator|.
name|transformParameters
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|tr
operator|.
name|setParameter
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|ti
operator|.
name|outProps
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|tr
operator|.
name|setOutputProperty
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
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
literal|"Could not set properties for transfomer"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|Object
name|unmarshalFromReader
parameter_list|(
name|Unmarshaller
name|unmarshaller
parameter_list|,
name|XMLStreamReader
name|reader
parameter_list|,
name|MediaType
name|mt
parameter_list|)
throws|throws
name|JAXBException
block|{
name|CachedOutputStream
name|out
init|=
operator|new
name|CachedOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|XMLStreamWriter
name|writer
init|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|out
argument_list|)
decl_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
operator|new
name|StaxSource
argument_list|(
name|reader
argument_list|)
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|writer
operator|.
name|writeEndDocument
argument_list|()
expr_stmt|;
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|unmarshalFromInputStream
argument_list|(
name|unmarshaller
argument_list|,
name|out
operator|.
name|getInputStream
argument_list|()
argument_list|,
name|mt
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
name|ExceptionUtils
operator|.
name|toBadRequestException
argument_list|(
name|ex
argument_list|,
literal|null
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|void
name|marshalToWriter
parameter_list|(
name|Marshaller
name|ms
parameter_list|,
name|Object
name|obj
parameter_list|,
name|XMLStreamWriter
name|writer
parameter_list|,
name|MediaType
name|mt
parameter_list|)
throws|throws
name|Exception
block|{
name|CachedOutputStream
name|out
init|=
operator|new
name|CachedOutputStream
argument_list|()
decl_stmt|;
name|marshalToOutputStream
argument_list|(
name|ms
argument_list|,
name|obj
argument_list|,
name|out
argument_list|,
name|mt
argument_list|)
expr_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|out
operator|.
name|getInputStream
argument_list|()
argument_list|)
argument_list|,
name|writer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|addAttachmentMarshaller
parameter_list|(
name|Marshaller
name|ms
parameter_list|)
block|{
comment|// complete
block|}
annotation|@
name|Override
specifier|protected
name|void
name|marshalToOutputStream
parameter_list|(
name|Marshaller
name|ms
parameter_list|,
name|Object
name|obj
parameter_list|,
name|OutputStream
name|os
parameter_list|,
name|MediaType
name|mt
parameter_list|)
throws|throws
name|Exception
block|{
name|Templates
name|t
init|=
name|createTemplates
argument_list|(
name|getOutTemplates
argument_list|(
name|mt
argument_list|)
argument_list|,
name|outParamsMap
argument_list|,
name|outProperties
argument_list|)
decl_stmt|;
if|if
condition|(
name|t
operator|==
literal|null
operator|&&
name|supportJaxbOnly
condition|)
block|{
name|super
operator|.
name|marshalToOutputStream
argument_list|(
name|ms
argument_list|,
name|obj
argument_list|,
name|os
argument_list|,
name|mt
argument_list|)
expr_stmt|;
return|return;
block|}
name|TransformerHandler
name|th
init|=
literal|null
decl_stmt|;
try|try
block|{
name|th
operator|=
name|factory
operator|.
name|newTransformerHandler
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TransformerConfigurationException
name|ex
parameter_list|)
block|{
name|TemplatesImpl
name|ti
init|=
operator|(
name|TemplatesImpl
operator|)
name|t
decl_stmt|;
name|th
operator|=
name|factory
operator|.
name|newTransformerHandler
argument_list|(
name|ti
operator|.
name|getTemplates
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|trySettingProperties
argument_list|(
name|th
argument_list|,
name|ti
argument_list|)
expr_stmt|;
block|}
name|Result
name|result
init|=
operator|new
name|StreamResult
argument_list|(
name|os
argument_list|)
decl_stmt|;
if|if
condition|(
name|systemId
operator|!=
literal|null
condition|)
block|{
name|result
operator|.
name|setSystemId
argument_list|(
name|systemId
argument_list|)
expr_stmt|;
block|}
name|th
operator|.
name|setResult
argument_list|(
name|result
argument_list|)
expr_stmt|;
if|if
condition|(
name|getContext
argument_list|()
operator|==
literal|null
condition|)
block|{
name|th
operator|.
name|startDocument
argument_list|()
expr_stmt|;
block|}
name|ms
operator|.
name|marshal
argument_list|(
name|obj
argument_list|,
name|th
argument_list|)
expr_stmt|;
if|if
condition|(
name|getContext
argument_list|()
operator|==
literal|null
condition|)
block|{
name|th
operator|.
name|endDocument
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setOutTemplate
parameter_list|(
name|String
name|loc
parameter_list|)
block|{
name|outTemplates
operator|=
name|createTemplates
argument_list|(
name|loc
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setInTemplate
parameter_list|(
name|String
name|loc
parameter_list|)
block|{
name|inTemplates
operator|=
name|createTemplates
argument_list|(
name|loc
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setInMediaTemplates
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
parameter_list|)
block|{
name|inMediaTemplates
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Templates
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|inMediaTemplates
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|createTemplates
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setOutMediaTemplates
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
parameter_list|)
block|{
name|outMediaTemplates
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Templates
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|outMediaTemplates
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|createTemplates
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setResolver
parameter_list|(
name|URIResolver
name|resolver
parameter_list|)
block|{
name|uriResolver
operator|=
name|resolver
expr_stmt|;
if|if
condition|(
name|factory
operator|!=
literal|null
condition|)
block|{
name|factory
operator|.
name|setURIResolver
argument_list|(
name|uriResolver
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setSystemId
parameter_list|(
name|String
name|system
parameter_list|)
block|{
name|systemId
operator|=
name|system
expr_stmt|;
block|}
specifier|public
name|void
name|setInParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|inParams
parameter_list|)
block|{
name|this
operator|.
name|inParamsMap
operator|=
name|inParams
expr_stmt|;
block|}
specifier|public
name|void
name|setOutParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|outParams
parameter_list|)
block|{
name|this
operator|.
name|outParamsMap
operator|=
name|outParams
expr_stmt|;
block|}
specifier|public
name|void
name|setInProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|inProps
parameter_list|)
block|{
name|this
operator|.
name|inProperties
operator|=
name|inProps
expr_stmt|;
block|}
specifier|public
name|void
name|setOutProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|outProps
parameter_list|)
block|{
name|this
operator|.
name|outProperties
operator|=
name|outProps
expr_stmt|;
block|}
specifier|public
name|void
name|setInClassNames
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|classNames
parameter_list|)
block|{
name|inClassesToHandle
operator|=
name|classNames
expr_stmt|;
block|}
specifier|public
name|boolean
name|inClassCanBeHandled
parameter_list|(
name|String
name|className
parameter_list|)
block|{
return|return
name|inClassesToHandle
operator|!=
literal|null
operator|&&
name|inClassesToHandle
operator|.
name|contains
argument_list|(
name|className
argument_list|)
return|;
block|}
specifier|public
name|void
name|setOutClassNames
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|classNames
parameter_list|)
block|{
name|outClassesToHandle
operator|=
name|classNames
expr_stmt|;
block|}
specifier|public
name|boolean
name|outClassCanBeHandled
parameter_list|(
name|String
name|className
parameter_list|)
block|{
return|return
name|outClassesToHandle
operator|!=
literal|null
operator|&&
name|outClassesToHandle
operator|.
name|contains
argument_list|(
name|className
argument_list|)
return|;
block|}
specifier|protected
name|Templates
name|createTemplates
parameter_list|(
name|Templates
name|templates
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|configuredParams
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|outProps
parameter_list|)
block|{
if|if
condition|(
name|templates
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|supportJaxbOnly
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
name|LOG
operator|.
name|severe
argument_list|(
literal|"No template is available"
argument_list|)
expr_stmt|;
throw|throw
name|ExceptionUtils
operator|.
name|toInternalServerErrorException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
throw|;
block|}
block|}
name|TemplatesImpl
name|templ
init|=
operator|new
name|TemplatesImpl
argument_list|(
name|templates
argument_list|,
name|uriResolver
argument_list|)
decl_stmt|;
name|MessageContext
name|mc
init|=
name|getContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|mc
operator|!=
literal|null
condition|)
block|{
name|UriInfo
name|ui
init|=
name|mc
operator|.
name|getUriInfo
argument_list|()
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
init|=
name|ui
operator|.
name|getPathParameters
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|entry
range|:
name|params
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|int
name|ind
init|=
name|value
operator|.
name|indexOf
argument_list|(
literal|";"
argument_list|)
decl_stmt|;
if|if
condition|(
name|ind
operator|>
literal|0
condition|)
block|{
name|value
operator|=
name|value
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|ind
argument_list|)
expr_stmt|;
block|}
name|templ
operator|.
name|setTransformerParameter
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|PathSegment
argument_list|>
name|segments
init|=
name|ui
operator|.
name|getPathSegments
argument_list|()
decl_stmt|;
if|if
condition|(
name|segments
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|setTransformParameters
argument_list|(
name|templ
argument_list|,
name|segments
operator|.
name|get
argument_list|(
name|segments
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
operator|.
name|getMatrixParameters
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|setTransformParameters
argument_list|(
name|templ
argument_list|,
name|ui
operator|.
name|getQueryParameters
argument_list|()
argument_list|)
expr_stmt|;
name|templ
operator|.
name|setTransformerParameter
argument_list|(
name|ABSOLUTE_PATH_PARAMETER
argument_list|,
name|ui
operator|.
name|getAbsolutePath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|templ
operator|.
name|setTransformerParameter
argument_list|(
name|RELATIVE_PATH_PARAMETER
argument_list|,
name|ui
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|templ
operator|.
name|setTransformerParameter
argument_list|(
name|BASE_PATH_PARAMETER
argument_list|,
name|ui
operator|.
name|getBaseUri
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|configuredParams
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|configuredParams
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|templ
operator|.
name|setTransformerParameter
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|outProps
operator|!=
literal|null
condition|)
block|{
name|templ
operator|.
name|setOutProperties
argument_list|(
name|outProps
argument_list|)
expr_stmt|;
block|}
return|return
name|templ
return|;
block|}
specifier|private
name|void
name|setTransformParameters
parameter_list|(
name|TemplatesImpl
name|templ
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|entry
range|:
name|params
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|templ
operator|.
name|setTransformerParameter
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|Templates
name|createTemplates
parameter_list|(
name|String
name|loc
parameter_list|)
block|{
try|try
block|{
name|InputStream
name|is
init|=
name|ResourceUtils
operator|.
name|getResourceStream
argument_list|(
name|loc
argument_list|,
name|this
operator|.
name|getBus
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Reader
name|r
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|is
argument_list|,
literal|"UTF-8"
argument_list|)
argument_list|)
decl_stmt|;
name|Source
name|source
init|=
operator|new
name|StreamSource
argument_list|(
name|r
argument_list|)
decl_stmt|;
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
name|factory
operator|=
operator|(
name|SAXTransformerFactory
operator|)
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
if|if
condition|(
name|uriResolver
operator|!=
literal|null
condition|)
block|{
name|factory
operator|.
name|setURIResolver
argument_list|(
name|uriResolver
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|factory
operator|.
name|newTemplates
argument_list|(
name|source
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"No template can be created : "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
specifier|static
class|class
name|TemplatesImpl
implements|implements
name|Templates
block|{
specifier|private
name|Templates
name|templates
decl_stmt|;
specifier|private
name|URIResolver
name|resolver
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|transformParameters
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|outProps
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|TemplatesImpl
parameter_list|(
name|Templates
name|templates
parameter_list|,
name|URIResolver
name|resolver
parameter_list|)
block|{
name|this
operator|.
name|templates
operator|=
name|templates
expr_stmt|;
name|this
operator|.
name|resolver
operator|=
name|resolver
expr_stmt|;
block|}
specifier|public
name|Templates
name|getTemplates
parameter_list|()
block|{
return|return
name|templates
return|;
block|}
specifier|public
name|void
name|setTransformerParameter
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|transformParameters
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setOutProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|props
parameter_list|)
block|{
name|this
operator|.
name|outProps
operator|=
name|props
expr_stmt|;
block|}
specifier|public
name|Properties
name|getOutputProperties
parameter_list|()
block|{
return|return
name|templates
operator|.
name|getOutputProperties
argument_list|()
return|;
block|}
specifier|public
name|Transformer
name|newTransformer
parameter_list|()
throws|throws
name|TransformerConfigurationException
block|{
name|Transformer
name|tr
init|=
name|templates
operator|.
name|newTransformer
argument_list|()
decl_stmt|;
name|tr
operator|.
name|setURIResolver
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|transformParameters
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|tr
operator|.
name|setParameter
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|outProps
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|tr
operator|.
name|setOutputProperty
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|tr
return|;
block|}
block|}
block|}
end_class

end_unit

