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
name|addressing
package|;
end_package

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
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|ParserConfigurationException
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
name|dom
operator|.
name|DOMResult
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|EndpointReference
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|wsaddressing
operator|.
name|W3CEndpointReference
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

begin_comment
comment|// importation convention: if the same class name is used for
end_comment

begin_comment
comment|// 2005/08 and 2004/08, then the former version is imported
end_comment

begin_comment
comment|// and the latter is fully qualified when used
end_comment

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
name|helpers
operator|.
name|XMLUtils
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
name|W3CDOMStreamReader
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
name|addressing
operator|.
name|v200408
operator|.
name|AttributedQName
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
name|addressing
operator|.
name|v200408
operator|.
name|AttributedURI
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
name|addressing
operator|.
name|v200408
operator|.
name|ObjectFactory
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
name|addressing
operator|.
name|v200408
operator|.
name|Relationship
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
name|addressing
operator|.
name|v200408
operator|.
name|ServiceNameType
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
name|EndpointReferenceUtils
import|;
end_import

begin_comment
comment|/**  * This class is responsible for transforming between the native   * WS-Addressing schema version (i.e. 2005/08) and exposed  * version (currently may be 2005/08 or 2004/08).  *<p>  * The native version is that used throughout the stack, were the  * WS-A types are represented via the JAXB generated types for the  * 2005/08 schema.  *<p>  * The exposed version is that used when the WS-A types are   * externalized, i.e. are encoded in the headers of outgoing   * messages. For outgoing requests, the exposed version is   * determined from configuration. For outgoing responses, the  * exposed version is determined by the exposed version of  * the corresponding request.  *<p>  * The motivation for using different native and exposed types  * is usually to facilitate a WS-* standard based on an earlier   * version of WS-Adressing (for example WS-RM depends on the  * 2004/08 version).  */
end_comment

begin_class
specifier|public
class|class
name|VersionTransformer
block|{
specifier|protected
specifier|static
specifier|final
name|String
name|NATIVE_VERSION
init|=
name|Names
operator|.
name|WSA_NAMESPACE_NAME
decl_stmt|;
comment|/**      * Constructor.      */
specifier|public
name|VersionTransformer
parameter_list|()
block|{     }
comment|/**      * @param namespace a namspace URI to consider      * @return true if th WS-Addressing version specified by the namespace       * URI is supported      */
specifier|public
name|boolean
name|isSupported
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
return|return
name|NATIVE_VERSION
operator|.
name|equals
argument_list|(
name|namespace
argument_list|)
operator|||
name|Names200408
operator|.
name|WSA_NAMESPACE_NAME
operator|.
name|equals
argument_list|(
name|namespace
argument_list|)
return|;
block|}
comment|/**      * Convert from 2005/08 AttributedURI to 2004/08 AttributedURI.      *       * @param internal the 2005/08 AttributedURIType      * @return an equivalent 2004/08 AttributedURI      */
specifier|public
specifier|static
name|AttributedURI
name|convert
parameter_list|(
name|AttributedURIType
name|internal
parameter_list|)
block|{
name|AttributedURI
name|exposed
init|=
name|Names200408
operator|.
name|WSA_OBJECT_FACTORY
operator|.
name|createAttributedURI
argument_list|()
decl_stmt|;
name|String
name|exposedValue
init|=
name|Names
operator|.
name|WSA_ANONYMOUS_ADDRESS
operator|.
name|equals
argument_list|(
name|internal
operator|.
name|getValue
argument_list|()
argument_list|)
condition|?
name|Names200408
operator|.
name|WSA_ANONYMOUS_ADDRESS
else|:
name|Names
operator|.
name|WSA_NONE_ADDRESS
operator|.
name|equals
argument_list|(
name|internal
operator|.
name|getValue
argument_list|()
argument_list|)
condition|?
name|Names200408
operator|.
name|WSA_NONE_ADDRESS
else|:
name|internal
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|exposed
operator|.
name|setValue
argument_list|(
name|exposedValue
argument_list|)
expr_stmt|;
name|putAll
argument_list|(
name|exposed
operator|.
name|getOtherAttributes
argument_list|()
argument_list|,
name|internal
operator|.
name|getOtherAttributes
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|exposed
return|;
block|}
comment|/**      * Convert from 2004/08 AttributedURI to 2005/08 AttributedURI.      *       * @param exposed the 2004/08 AttributedURI      * @return an equivalent 2005/08 AttributedURIType      */
specifier|public
specifier|static
name|AttributedURIType
name|convert
parameter_list|(
name|AttributedURI
name|exposed
parameter_list|)
block|{
name|AttributedURIType
name|internal
init|=
name|ContextUtils
operator|.
name|WSA_OBJECT_FACTORY
operator|.
name|createAttributedURIType
argument_list|()
decl_stmt|;
name|String
name|internalValue
init|=
name|Names200408
operator|.
name|WSA_ANONYMOUS_ADDRESS
operator|.
name|equals
argument_list|(
name|exposed
operator|.
name|getValue
argument_list|()
argument_list|)
condition|?
name|Names
operator|.
name|WSA_ANONYMOUS_ADDRESS
else|:
name|Names200408
operator|.
name|WSA_NONE_ADDRESS
operator|.
name|equals
argument_list|(
name|exposed
operator|.
name|getValue
argument_list|()
argument_list|)
condition|?
name|Names
operator|.
name|WSA_NONE_ADDRESS
else|:
name|exposed
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|internal
operator|.
name|setValue
argument_list|(
name|internalValue
argument_list|)
expr_stmt|;
name|putAll
argument_list|(
name|internal
operator|.
name|getOtherAttributes
argument_list|()
argument_list|,
name|exposed
operator|.
name|getOtherAttributes
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|internal
return|;
block|}
comment|/**      * Convert from 2005/08 EndpointReferenceType to 2004/08       * EndpointReferenceType.      *       * @param internal the 2005/08 EndpointReferenceType      * @return an equivalent 2004/08 EndpointReferenceType      */
specifier|public
specifier|static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|v200408
operator|.
name|EndpointReferenceType
name|convert
parameter_list|(
name|EndpointReferenceType
name|internal
parameter_list|)
block|{
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|v200408
operator|.
name|EndpointReferenceType
name|exposed
init|=
name|Names200408
operator|.
name|WSA_OBJECT_FACTORY
operator|.
name|createEndpointReferenceType
argument_list|()
decl_stmt|;
name|exposed
operator|.
name|setAddress
argument_list|(
name|convert
argument_list|(
name|internal
operator|.
name|getAddress
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|exposed
operator|.
name|setReferenceParameters
argument_list|(
name|convert
argument_list|(
name|internal
operator|.
name|getReferenceParameters
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|QName
name|serviceQName
init|=
name|EndpointReferenceUtils
operator|.
name|getServiceName
argument_list|(
name|internal
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|serviceQName
operator|!=
literal|null
condition|)
block|{
name|ServiceNameType
name|serviceName
init|=
name|Names200408
operator|.
name|WSA_OBJECT_FACTORY
operator|.
name|createServiceNameType
argument_list|()
decl_stmt|;
name|serviceName
operator|.
name|setValue
argument_list|(
name|serviceQName
argument_list|)
expr_stmt|;
name|exposed
operator|.
name|setServiceName
argument_list|(
name|serviceName
argument_list|)
expr_stmt|;
block|}
name|String
name|portLocalName
init|=
name|EndpointReferenceUtils
operator|.
name|getPortName
argument_list|(
name|internal
argument_list|)
decl_stmt|;
if|if
condition|(
name|portLocalName
operator|!=
literal|null
condition|)
block|{
name|String
name|namespace
init|=
name|serviceQName
operator|.
name|getNamespaceURI
argument_list|()
operator|!=
literal|null
condition|?
name|serviceQName
operator|.
name|getNamespaceURI
argument_list|()
else|:
name|Names
operator|.
name|WSDL_INSTANCE_NAMESPACE_NAME
decl_stmt|;
name|QName
name|portQName
init|=
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
name|portLocalName
argument_list|)
decl_stmt|;
name|AttributedQName
name|portType
init|=
name|Names200408
operator|.
name|WSA_OBJECT_FACTORY
operator|.
name|createAttributedQName
argument_list|()
decl_stmt|;
name|portType
operator|.
name|setValue
argument_list|(
name|portQName
argument_list|)
expr_stmt|;
name|exposed
operator|.
name|setPortType
argument_list|(
name|portType
argument_list|)
expr_stmt|;
block|}
comment|// no direct analogue for Metadata
name|addAll
argument_list|(
name|exposed
operator|.
name|getAny
argument_list|()
argument_list|,
name|internal
operator|.
name|getAny
argument_list|()
argument_list|)
expr_stmt|;
name|putAll
argument_list|(
name|exposed
operator|.
name|getOtherAttributes
argument_list|()
argument_list|,
name|internal
operator|.
name|getOtherAttributes
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|exposed
return|;
block|}
comment|/**      * Convert from 2004/08 EndpointReferenceType to 2005/08       * EndpointReferenceType.      *       * @param exposed the 2004/08 EndpointReferenceType      * @return an equivalent 2005/08 EndpointReferenceType      */
specifier|public
specifier|static
name|EndpointReferenceType
name|convert
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|v200408
operator|.
name|EndpointReferenceType
name|exposed
parameter_list|)
block|{
name|EndpointReferenceType
name|internal
init|=
name|ContextUtils
operator|.
name|WSA_OBJECT_FACTORY
operator|.
name|createEndpointReferenceType
argument_list|()
decl_stmt|;
name|internal
operator|.
name|setAddress
argument_list|(
name|convert
argument_list|(
name|exposed
operator|.
name|getAddress
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|internal
operator|.
name|setReferenceParameters
argument_list|(
name|convert
argument_list|(
name|exposed
operator|.
name|getReferenceParameters
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|ServiceNameType
name|serviceName
init|=
name|exposed
operator|.
name|getServiceName
argument_list|()
decl_stmt|;
name|AttributedQName
name|portName
init|=
name|exposed
operator|.
name|getPortType
argument_list|()
decl_stmt|;
if|if
condition|(
name|serviceName
operator|!=
literal|null
operator|&&
name|portName
operator|!=
literal|null
condition|)
block|{
name|EndpointReferenceUtils
operator|.
name|setServiceAndPortName
argument_list|(
name|internal
argument_list|,
name|serviceName
operator|.
name|getValue
argument_list|()
argument_list|,
name|portName
operator|.
name|getValue
argument_list|()
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// no direct analogue for ReferenceProperties
name|addAll
argument_list|(
name|internal
operator|.
name|getAny
argument_list|()
argument_list|,
name|exposed
operator|.
name|getAny
argument_list|()
argument_list|)
expr_stmt|;
name|putAll
argument_list|(
name|internal
operator|.
name|getOtherAttributes
argument_list|()
argument_list|,
name|exposed
operator|.
name|getOtherAttributes
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|internal
return|;
block|}
comment|/**      * Convert from EndpointReference to CXF internal 2005/08 EndpointReferenceType      *       * @param external the javax.xml.ws.EndpointReference      * @return CXF internal 2005/08 EndpointReferenceType      */
specifier|public
specifier|static
name|EndpointReferenceType
name|convertToInternal
parameter_list|(
name|EndpointReference
name|external
parameter_list|)
block|{
if|if
condition|(
name|external
operator|instanceof
name|W3CEndpointReference
condition|)
block|{
try|try
block|{
name|Document
name|doc
init|=
name|XMLUtils
operator|.
name|newDocument
argument_list|()
decl_stmt|;
name|DOMResult
name|result
init|=
operator|new
name|DOMResult
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|external
operator|.
name|writeTo
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|W3CDOMStreamReader
name|reader
init|=
operator|new
name|W3CDOMStreamReader
argument_list|(
name|doc
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
decl_stmt|;
comment|// CXF internal 2005/08 EndpointReferenceType should be
comment|// compatible with W3CEndpointReference
comment|//jaxContext = ContextUtils.getJAXBContext();
name|JAXBContext
name|jaxbContext
init|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
operator|new
name|Class
index|[]
block|{
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|ObjectFactory
operator|.
name|class
block|}
argument_list|)
decl_stmt|;
name|EndpointReferenceType
name|internal
init|=
name|jaxbContext
operator|.
name|createUnmarshaller
argument_list|()
operator|.
name|unmarshal
argument_list|(
name|reader
argument_list|,
name|EndpointReferenceType
operator|.
name|class
argument_list|)
operator|.
name|getValue
argument_list|()
decl_stmt|;
return|return
name|internal
return|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
comment|// TODO Auto-generated catch block
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParserConfigurationException
name|e
parameter_list|)
block|{
comment|// TODO Auto-generated catch block
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
else|else
block|{
comment|//TODO: 200408
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Convert from 2005/08 ReferenceParametersType to 2004/08      * ReferenceParametersType.      *       * @param internal the 2005/08 ReferenceParametersType      * @return an equivalent 2004/08 ReferenceParametersType      */
specifier|public
specifier|static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|v200408
operator|.
name|ReferenceParametersType
name|convert
parameter_list|(
name|ReferenceParametersType
name|internal
parameter_list|)
block|{
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|v200408
operator|.
name|ReferenceParametersType
name|exposed
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|internal
operator|!=
literal|null
condition|)
block|{
name|exposed
operator|=
name|Names200408
operator|.
name|WSA_OBJECT_FACTORY
operator|.
name|createReferenceParametersType
argument_list|()
expr_stmt|;
name|addAll
argument_list|(
name|exposed
operator|.
name|getAny
argument_list|()
argument_list|,
name|internal
operator|.
name|getAny
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|exposed
return|;
block|}
comment|/**      * Convert from 2004/08 ReferenceParametersType to 2005/08      * ReferenceParametersType.      *       * @param exposed the 2004/08 ReferenceParametersType      * @return an equivalent 2005/08 ReferenceParametersType      */
specifier|public
specifier|static
name|ReferenceParametersType
name|convert
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|v200408
operator|.
name|ReferenceParametersType
name|exposed
parameter_list|)
block|{
name|ReferenceParametersType
name|internal
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|exposed
operator|!=
literal|null
condition|)
block|{
name|internal
operator|=
name|ContextUtils
operator|.
name|WSA_OBJECT_FACTORY
operator|.
name|createReferenceParametersType
argument_list|()
expr_stmt|;
name|addAll
argument_list|(
name|internal
operator|.
name|getAny
argument_list|()
argument_list|,
name|exposed
operator|.
name|getAny
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|internal
return|;
block|}
comment|/**      * Convert from 2005/08 RelatesToType to 2004/08 Relationship.      *       * @param internal the 2005/08 RelatesToType      * @return an equivalent 2004/08 Relationship      */
specifier|public
specifier|static
name|Relationship
name|convert
parameter_list|(
name|RelatesToType
name|internal
parameter_list|)
block|{
name|Relationship
name|exposed
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|internal
operator|!=
literal|null
condition|)
block|{
name|exposed
operator|=
name|Names200408
operator|.
name|WSA_OBJECT_FACTORY
operator|.
name|createRelationship
argument_list|()
expr_stmt|;
name|exposed
operator|.
name|setValue
argument_list|(
name|internal
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|internalRelationshipType
init|=
name|internal
operator|.
name|getRelationshipType
argument_list|()
decl_stmt|;
if|if
condition|(
name|internalRelationshipType
operator|!=
literal|null
condition|)
block|{
name|QName
name|exposedRelationshipType
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|Names
operator|.
name|WSA_RELATIONSHIP_REPLY
operator|.
name|equals
argument_list|(
name|internalRelationshipType
argument_list|)
condition|)
block|{
name|exposedRelationshipType
operator|=
operator|new
name|QName
argument_list|(
name|internalRelationshipType
argument_list|)
expr_stmt|;
block|}
name|exposed
operator|.
name|setRelationshipType
argument_list|(
name|exposedRelationshipType
argument_list|)
expr_stmt|;
block|}
name|putAll
argument_list|(
name|exposed
operator|.
name|getOtherAttributes
argument_list|()
argument_list|,
name|internal
operator|.
name|getOtherAttributes
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|exposed
return|;
block|}
comment|/**      * Convert from 2004/08 Relationship to 2005/08 RelatesToType.      *       * @param exposed the 2004/08 Relationship      * @return an equivalent 2005/08 RelatesToType      */
specifier|public
specifier|static
name|RelatesToType
name|convert
parameter_list|(
name|Relationship
name|exposed
parameter_list|)
block|{
name|RelatesToType
name|internal
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|exposed
operator|!=
literal|null
condition|)
block|{
name|internal
operator|=
name|ContextUtils
operator|.
name|WSA_OBJECT_FACTORY
operator|.
name|createRelatesToType
argument_list|()
expr_stmt|;
name|internal
operator|.
name|setValue
argument_list|(
name|exposed
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|QName
name|exposedRelationshipType
init|=
name|exposed
operator|.
name|getRelationshipType
argument_list|()
decl_stmt|;
if|if
condition|(
name|exposedRelationshipType
operator|!=
literal|null
condition|)
block|{
name|String
name|internalRelationshipType
init|=
name|Names
operator|.
name|WSA_REPLY_NAME
operator|.
name|equals
argument_list|(
name|exposedRelationshipType
operator|.
name|getLocalPart
argument_list|()
argument_list|)
condition|?
name|Names
operator|.
name|WSA_RELATIONSHIP_REPLY
else|:
name|exposedRelationshipType
operator|.
name|toString
argument_list|()
decl_stmt|;
name|internal
operator|.
name|setRelationshipType
argument_list|(
name|internalRelationshipType
argument_list|)
expr_stmt|;
block|}
name|internal
operator|.
name|getOtherAttributes
argument_list|()
operator|.
name|putAll
argument_list|(
name|exposed
operator|.
name|getOtherAttributes
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|internal
return|;
block|}
comment|/**       * @param exposedURI specifies the version WS-Addressing      * @return JABXContext for the exposed namespace URI      */
specifier|public
specifier|static
name|JAXBContext
name|getExposedJAXBContext
parameter_list|(
name|String
name|exposedURI
parameter_list|)
throws|throws
name|JAXBException
block|{
return|return
name|NATIVE_VERSION
operator|.
name|equals
argument_list|(
name|exposedURI
argument_list|)
condition|?
name|ContextUtils
operator|.
name|getJAXBContext
argument_list|()
else|:
name|Names200408
operator|.
name|getJAXBContext
argument_list|()
return|;
block|}
comment|/**      * Put all entries from one map into another.      *       * @param to target map      * @param from source map      */
specifier|private
specifier|static
name|void
name|putAll
parameter_list|(
name|Map
argument_list|<
name|QName
argument_list|,
name|String
argument_list|>
name|to
parameter_list|,
name|Map
argument_list|<
name|QName
argument_list|,
name|String
argument_list|>
name|from
parameter_list|)
block|{
if|if
condition|(
name|from
operator|!=
literal|null
condition|)
block|{
name|to
operator|.
name|putAll
argument_list|(
name|from
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Add all entries from one list into another.      *       * @param to target list      * @param from source list      */
specifier|private
specifier|static
name|void
name|addAll
parameter_list|(
name|List
argument_list|<
name|Object
argument_list|>
name|to
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|from
parameter_list|)
block|{
if|if
condition|(
name|from
operator|!=
literal|null
condition|)
block|{
name|to
operator|.
name|addAll
argument_list|(
name|from
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Holder for 2004/08 Names      */
specifier|public
specifier|static
class|class
name|Names200408
block|{
specifier|public
specifier|static
specifier|final
name|String
name|WSA_NAMESPACE_NAME
init|=
literal|"http://schemas.xmlsoap.org/ws/2004/08/addressing"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSA_ANONYMOUS_ADDRESS
init|=
name|WSA_NAMESPACE_NAME
operator|+
literal|"/role/anonymous"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|WSA_NONE_ADDRESS
init|=
name|WSA_NAMESPACE_NAME
operator|+
literal|"/role/none"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|ObjectFactory
name|WSA_OBJECT_FACTORY
init|=
operator|new
name|ObjectFactory
argument_list|()
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Class
argument_list|<
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|v200408
operator|.
name|EndpointReferenceType
argument_list|>
name|EPR_TYPE
init|=
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|v200408
operator|.
name|EndpointReferenceType
operator|.
name|class
decl_stmt|;
specifier|private
specifier|static
name|JAXBContext
name|jaxbContext
decl_stmt|;
specifier|protected
name|Names200408
parameter_list|()
block|{         }
comment|/**          * Retrieve a JAXBContext for marshalling and unmarshalling JAXB generated          * types for the 2004/08 version.          *          * @return a JAXBContext           */
specifier|public
specifier|static
name|JAXBContext
name|getJAXBContext
parameter_list|()
throws|throws
name|JAXBException
block|{
synchronized|synchronized
init|(
name|Names200408
operator|.
name|class
init|)
block|{
if|if
condition|(
name|jaxbContext
operator|==
literal|null
condition|)
block|{
name|Class
name|clz
init|=
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|addressing
operator|.
name|v200408
operator|.
name|ObjectFactory
operator|.
name|class
decl_stmt|;
name|jaxbContext
operator|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|PackageUtils
operator|.
name|getPackageName
argument_list|(
name|clz
argument_list|)
argument_list|,
name|clz
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|jaxbContext
return|;
block|}
comment|/**          * Set the encapsulated JAXBContext (used by unit tests).          *           * @param ctx JAXBContext           */
specifier|public
specifier|static
name|void
name|setJAXBContext
parameter_list|(
name|JAXBContext
name|ctx
parameter_list|)
throws|throws
name|JAXBException
block|{
synchronized|synchronized
init|(
name|Names200408
operator|.
name|class
init|)
block|{
name|jaxbContext
operator|=
name|ctx
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

