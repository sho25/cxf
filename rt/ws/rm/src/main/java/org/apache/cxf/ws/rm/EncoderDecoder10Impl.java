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
name|rm
package|;
end_package

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
name|Attr
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
name|helpers
operator|.
name|DOMUtils
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
name|VersionTransformer
operator|.
name|Names200408
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
name|rm
operator|.
name|v200702
operator|.
name|AckRequestedType
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
name|rm
operator|.
name|v200702
operator|.
name|CreateSequenceResponseType
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
name|rm
operator|.
name|v200702
operator|.
name|CreateSequenceType
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
name|rm
operator|.
name|v200702
operator|.
name|Identifier
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
name|rm
operator|.
name|v200702
operator|.
name|SequenceAcknowledgement
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
name|rm
operator|.
name|v200702
operator|.
name|SequenceFaultType
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
name|rm
operator|.
name|v200702
operator|.
name|SequenceType
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
name|rm
operator|.
name|v200702
operator|.
name|TerminateSequenceType
import|;
end_import

begin_comment
comment|/**  * WS-ReliableMessaging 1.0 encoding and decoding. This converts between the standard WS-RM objects and the  * 1.0 representation using the WS-Addressing 200408 namespace specified in the WS-RM 1.0 recommendation.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|EncoderDecoder10Impl
implements|implements
name|EncoderDecoder
block|{
specifier|public
specifier|static
specifier|final
name|EncoderDecoder10Impl
name|INSTANCE
init|=
operator|new
name|EncoderDecoder10Impl
argument_list|()
decl_stmt|;
specifier|private
specifier|static
name|JAXBContext
name|jaxbContext
decl_stmt|;
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
name|EncoderDecoder10Impl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|EncoderDecoder10Impl
parameter_list|()
block|{     }
specifier|public
name|String
name|getWSRMNamespace
parameter_list|()
block|{
return|return
name|RM10Constants
operator|.
name|NAMESPACE_URI
return|;
block|}
specifier|public
name|String
name|getWSANamespace
parameter_list|()
block|{
return|return
name|Names200408
operator|.
name|WSA_NAMESPACE_NAME
return|;
block|}
specifier|public
name|RMConstants
name|getConstants
parameter_list|()
block|{
return|return
name|RM10Constants
operator|.
name|INSTANCE
return|;
block|}
specifier|public
name|Class
name|getCreateSequenceType
parameter_list|()
block|{
return|return
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|rm
operator|.
name|v200502
operator|.
name|CreateSequenceType
operator|.
name|class
return|;
block|}
specifier|public
name|Class
name|getCreateSequenceResponseType
parameter_list|()
block|{
return|return
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|rm
operator|.
name|v200502
operator|.
name|CreateSequenceResponseType
operator|.
name|class
return|;
block|}
specifier|public
name|Class
name|getTerminateSequenceType
parameter_list|()
block|{
return|return
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|rm
operator|.
name|v200502
operator|.
name|TerminateSequenceType
operator|.
name|class
return|;
block|}
specifier|private
specifier|static
name|JAXBContext
name|getContext
parameter_list|()
throws|throws
name|JAXBException
block|{
synchronized|synchronized
init|(
name|EncoderDecoder10Impl
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
name|clas
init|=
name|RMUtils
operator|.
name|getWSRM200502Factory
argument_list|()
operator|.
name|getClass
argument_list|()
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
name|clas
argument_list|)
argument_list|,
name|clas
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
specifier|public
name|Element
name|buildHeaders
parameter_list|(
name|RMProperties
name|rmps
parameter_list|,
name|QName
name|qname
parameter_list|)
throws|throws
name|JAXBException
block|{
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|createDocument
argument_list|()
decl_stmt|;
name|Element
name|header
init|=
name|doc
operator|.
name|createElementNS
argument_list|(
name|qname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|qname
operator|.
name|getLocalPart
argument_list|()
argument_list|)
decl_stmt|;
comment|// add WSRM namespace declaration to header, instead of
comment|// repeating in each individual child node
name|Attr
name|attr
init|=
name|doc
operator|.
name|createAttributeNS
argument_list|(
literal|"http://www.w3.org/2000/xmlns/"
argument_list|,
literal|"xmlns:"
operator|+
name|RMConstants
operator|.
name|NAMESPACE_PREFIX
argument_list|)
decl_stmt|;
name|attr
operator|.
name|setValue
argument_list|(
name|RM10Constants
operator|.
name|NAMESPACE_URI
argument_list|)
expr_stmt|;
name|header
operator|.
name|setAttributeNodeNS
argument_list|(
name|attr
argument_list|)
expr_stmt|;
name|Marshaller
name|marshaller
init|=
name|getContext
argument_list|()
operator|.
name|createMarshaller
argument_list|()
decl_stmt|;
name|marshaller
operator|.
name|setProperty
argument_list|(
name|Marshaller
operator|.
name|JAXB_FRAGMENT
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|SequenceType
name|seq
init|=
name|rmps
operator|.
name|getSequence
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|seq
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"encoding sequence into RM header"
argument_list|)
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|rm
operator|.
name|v200502
operator|.
name|SequenceType
name|toseq
init|=
name|VersionTransformer
operator|.
name|convert200502
argument_list|(
name|seq
argument_list|)
decl_stmt|;
name|JAXBElement
name|element
init|=
name|RMUtils
operator|.
name|getWSRM200502Factory
argument_list|()
operator|.
name|createSequence
argument_list|(
name|toseq
argument_list|)
decl_stmt|;
name|marshaller
operator|.
name|marshal
argument_list|(
name|element
argument_list|,
name|header
argument_list|)
expr_stmt|;
block|}
name|Collection
argument_list|<
name|SequenceAcknowledgement
argument_list|>
name|acks
init|=
name|rmps
operator|.
name|getAcks
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|acks
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"encoding sequence acknowledgement(s) into RM header"
argument_list|)
expr_stmt|;
for|for
control|(
name|SequenceAcknowledgement
name|ack
range|:
name|acks
control|)
block|{
name|marshaller
operator|.
name|marshal
argument_list|(
name|VersionTransformer
operator|.
name|convert200502
argument_list|(
name|ack
argument_list|)
argument_list|,
name|header
argument_list|)
expr_stmt|;
block|}
block|}
name|Collection
argument_list|<
name|AckRequestedType
argument_list|>
name|reqs
init|=
name|rmps
operator|.
name|getAcksRequested
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|reqs
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"encoding acknowledgement request(s) into RM header"
argument_list|)
expr_stmt|;
for|for
control|(
name|AckRequestedType
name|req
range|:
name|reqs
control|)
block|{
name|marshaller
operator|.
name|marshal
argument_list|(
name|RMUtils
operator|.
name|getWSRM200502Factory
argument_list|()
operator|.
name|createAckRequested
argument_list|(
name|VersionTransformer
operator|.
name|convert200502
argument_list|(
name|req
argument_list|)
argument_list|)
argument_list|,
name|header
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|header
return|;
block|}
specifier|public
name|Element
name|buildHeaderFault
parameter_list|(
name|SequenceFault
name|sf
parameter_list|,
name|QName
name|qname
parameter_list|)
throws|throws
name|JAXBException
block|{
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|createDocument
argument_list|()
decl_stmt|;
name|Element
name|header
init|=
name|doc
operator|.
name|createElementNS
argument_list|(
name|qname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|qname
operator|.
name|getLocalPart
argument_list|()
argument_list|)
decl_stmt|;
comment|// add WSRM namespace declaration to header, instead of
comment|// repeating in each individual child node
name|Attr
name|attr
init|=
name|doc
operator|.
name|createAttributeNS
argument_list|(
literal|"http://www.w3.org/2000/xmlns/"
argument_list|,
literal|"xmlns:"
operator|+
name|RMConstants
operator|.
name|NAMESPACE_PREFIX
argument_list|)
decl_stmt|;
name|attr
operator|.
name|setValue
argument_list|(
name|RM10Constants
operator|.
name|NAMESPACE_URI
argument_list|)
expr_stmt|;
name|header
operator|.
name|setAttributeNodeNS
argument_list|(
name|attr
argument_list|)
expr_stmt|;
name|Marshaller
name|marshaller
init|=
name|getContext
argument_list|()
operator|.
name|createMarshaller
argument_list|()
decl_stmt|;
name|marshaller
operator|.
name|setProperty
argument_list|(
name|Marshaller
operator|.
name|JAXB_FRAGMENT
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|QName
name|fqname
init|=
name|RM10Constants
operator|.
name|SEQUENCE_FAULT_QNAME
decl_stmt|;
name|marshaller
operator|.
name|marshal
argument_list|(
operator|new
name|JAXBElement
argument_list|<
name|SequenceFaultType
argument_list|>
argument_list|(
name|fqname
argument_list|,
name|SequenceFaultType
operator|.
name|class
argument_list|,
name|sf
operator|.
name|getSequenceFault
argument_list|()
argument_list|)
argument_list|,
name|header
argument_list|)
expr_stmt|;
return|return
name|header
return|;
block|}
specifier|public
name|Element
name|encodeSequenceAcknowledgement
parameter_list|(
name|SequenceAcknowledgement
name|ack
parameter_list|)
throws|throws
name|JAXBException
block|{
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|createDocument
argument_list|()
decl_stmt|;
name|Marshaller
name|marshaller
init|=
name|getContext
argument_list|()
operator|.
name|createMarshaller
argument_list|()
decl_stmt|;
name|marshaller
operator|.
name|marshal
argument_list|(
name|VersionTransformer
operator|.
name|convert200502
argument_list|(
name|ack
argument_list|)
argument_list|,
name|doc
argument_list|)
expr_stmt|;
return|return
operator|(
name|Element
operator|)
name|doc
operator|.
name|getFirstChild
argument_list|()
return|;
block|}
specifier|public
name|Element
name|encodeIdentifier
parameter_list|(
name|Identifier
name|id
parameter_list|)
throws|throws
name|JAXBException
block|{
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|createDocument
argument_list|()
decl_stmt|;
name|Marshaller
name|marshaller
init|=
name|getContext
argument_list|()
operator|.
name|createMarshaller
argument_list|()
decl_stmt|;
name|marshaller
operator|.
name|marshal
argument_list|(
name|VersionTransformer
operator|.
name|convert200502
argument_list|(
name|id
argument_list|)
argument_list|,
name|doc
argument_list|)
expr_stmt|;
return|return
operator|(
name|Element
operator|)
name|doc
operator|.
name|getFirstChild
argument_list|()
return|;
block|}
specifier|public
name|SequenceType
name|decodeSequenceType
parameter_list|(
name|Element
name|elem
parameter_list|)
throws|throws
name|JAXBException
block|{
name|Unmarshaller
name|unmarshaller
init|=
name|getContext
argument_list|()
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
name|JAXBElement
argument_list|<
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|rm
operator|.
name|v200502
operator|.
name|SequenceType
argument_list|>
name|jaxbElement
init|=
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
name|elem
argument_list|,
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|rm
operator|.
name|v200502
operator|.
name|SequenceType
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|VersionTransformer
operator|.
name|convert
argument_list|(
name|jaxbElement
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|SequenceAcknowledgement
name|decodeSequenceAcknowledgement
parameter_list|(
name|Element
name|elem
parameter_list|)
throws|throws
name|JAXBException
block|{
name|Unmarshaller
name|unmarshaller
init|=
name|getContext
argument_list|()
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|rm
operator|.
name|v200502
operator|.
name|SequenceAcknowledgement
name|ack
init|=
operator|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|rm
operator|.
name|v200502
operator|.
name|SequenceAcknowledgement
operator|)
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
name|elem
argument_list|)
decl_stmt|;
return|return
name|VersionTransformer
operator|.
name|convert
argument_list|(
name|ack
argument_list|)
return|;
block|}
specifier|public
name|AckRequestedType
name|decodeAckRequestedType
parameter_list|(
name|Element
name|elem
parameter_list|)
throws|throws
name|JAXBException
block|{
name|Unmarshaller
name|unmarshaller
init|=
name|getContext
argument_list|()
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
name|JAXBElement
argument_list|<
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|rm
operator|.
name|v200502
operator|.
name|AckRequestedType
argument_list|>
name|jaxbElement
init|=
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
name|elem
argument_list|,
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|rm
operator|.
name|v200502
operator|.
name|AckRequestedType
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|VersionTransformer
operator|.
name|convert
argument_list|(
name|jaxbElement
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|Object
name|convertToSend
parameter_list|(
name|CreateSequenceType
name|create
parameter_list|)
block|{
return|return
name|VersionTransformer
operator|.
name|convert200502
argument_list|(
name|create
argument_list|)
return|;
block|}
specifier|public
name|Object
name|convertToSend
parameter_list|(
name|CreateSequenceResponseType
name|create
parameter_list|)
block|{
return|return
name|VersionTransformer
operator|.
name|convert200502
argument_list|(
name|create
argument_list|)
return|;
block|}
specifier|public
name|Object
name|convertToSend
parameter_list|(
name|TerminateSequenceType
name|term
parameter_list|)
block|{
return|return
name|VersionTransformer
operator|.
name|convert200502
argument_list|(
name|term
argument_list|)
return|;
block|}
specifier|public
name|CreateSequenceType
name|convertReceivedCreateSequence
parameter_list|(
name|Object
name|create
parameter_list|)
block|{
return|return
name|VersionTransformer
operator|.
name|convert
argument_list|(
operator|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|rm
operator|.
name|v200502
operator|.
name|CreateSequenceType
operator|)
name|create
argument_list|)
return|;
block|}
specifier|public
name|CreateSequenceResponseType
name|convertReceivedCreateSequenceResponse
parameter_list|(
name|Object
name|response
parameter_list|)
block|{
return|return
name|VersionTransformer
operator|.
name|convert
argument_list|(
operator|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|rm
operator|.
name|v200502
operator|.
name|CreateSequenceResponseType
operator|)
name|response
argument_list|)
return|;
block|}
specifier|public
name|TerminateSequenceType
name|convertReceivedTerminateSequence
parameter_list|(
name|Object
name|term
parameter_list|)
block|{
return|return
name|VersionTransformer
operator|.
name|convert
argument_list|(
operator|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|rm
operator|.
name|v200502
operator|.
name|TerminateSequenceType
operator|)
name|term
argument_list|)
return|;
block|}
block|}
end_class

end_unit

