begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/////////////////////////////////////////////////////////////////////////
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Copyright University of Southampton IT Innovation Centre, 2009
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// Copyright in this library belongs to the University of Southampton
end_comment

begin_comment
comment|// University Road, Highfield, Southampton, UK, SO17 1BJ
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// This software may not be used, sold, licensed, transferred, copied
end_comment

begin_comment
comment|// or reproduced in whole or in part in any manner or form or in or
end_comment

begin_comment
comment|// on any media by any person other than in accordance with the terms
end_comment

begin_comment
comment|// of the License Agreement supplied with the software, or otherwise
end_comment

begin_comment
comment|// without the prior written consent of the copyright owners.
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|// This software is distributed WITHOUT ANY WARRANTY, without even the
end_comment

begin_comment
comment|// implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
end_comment

begin_comment
comment|// PURPOSE, except where stated in the License Agreement supplied with
end_comment

begin_comment
comment|// the software.
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|//  Created By :            Dominic Harries
end_comment

begin_comment
comment|//  Created Date :          2009-03-31
end_comment

begin_comment
comment|//  Created for Project :   BEinGRID
end_comment

begin_comment
comment|//
end_comment

begin_comment
comment|/////////////////////////////////////////////////////////////////////////
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
name|policy
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
name|javax
operator|.
name|wsdl
operator|.
name|extensions
operator|.
name|UnknownExtensibilityElement
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
name|stream
operator|.
name|XMLStreamException
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
name|service
operator|.
name|model
operator|.
name|BindingOperationInfo
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
name|EndpointInfo
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
name|Extensible
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
name|W3CDOMStreamWriter
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
name|policy
operator|.
name|attachment
operator|.
name|external
operator|.
name|PolicyAttachment
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Policy
import|;
end_import

begin_class
specifier|public
class|class
name|ServiceModelPolicyUpdater
block|{
specifier|private
name|EndpointInfo
name|ei
decl_stmt|;
specifier|public
name|ServiceModelPolicyUpdater
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|)
block|{
name|this
operator|.
name|ei
operator|=
name|ei
expr_stmt|;
block|}
specifier|public
name|void
name|addPolicyAttachments
parameter_list|(
name|Collection
argument_list|<
name|PolicyAttachment
argument_list|>
name|attachments
parameter_list|)
block|{
for|for
control|(
name|PolicyAttachment
name|pa
range|:
name|attachments
control|)
block|{
name|boolean
name|policyUsed
init|=
literal|false
decl_stmt|;
name|String
name|policyId
init|=
name|pa
operator|.
name|getPolicy
argument_list|()
operator|.
name|getId
argument_list|()
decl_stmt|;
comment|// Add wsp:PolicyReference to wsdl:binding/wsdl:operation
for|for
control|(
name|BindingOperationInfo
name|boi
range|:
name|ei
operator|.
name|getBinding
argument_list|()
operator|.
name|getOperations
argument_list|()
control|)
block|{
if|if
condition|(
name|pa
operator|.
name|appliesTo
argument_list|(
name|boi
argument_list|)
condition|)
block|{
name|addPolicyRef
argument_list|(
name|boi
argument_list|,
name|policyId
argument_list|)
expr_stmt|;
comment|// Add it to wsdl:portType/wsdl:operation too
name|addPolicyRef
argument_list|(
name|ei
operator|.
name|getInterface
argument_list|()
operator|.
name|getOperation
argument_list|(
name|boi
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|,
name|policyId
argument_list|)
expr_stmt|;
name|policyUsed
operator|=
literal|true
expr_stmt|;
block|}
block|}
comment|// Add wsp:Policy to top-level wsdl:definitions
if|if
condition|(
name|policyUsed
condition|)
block|{
name|addPolicy
argument_list|(
name|pa
operator|.
name|getPolicy
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|addPolicyRef
parameter_list|(
name|Extensible
name|ext
parameter_list|,
name|String
name|policyId
parameter_list|)
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
name|el
init|=
name|doc
operator|.
name|createElementNS
argument_list|(
name|Constants
operator|.
name|URI_POLICY_NS
argument_list|,
name|Constants
operator|.
name|ELEM_POLICY_REF
argument_list|)
decl_stmt|;
name|el
operator|.
name|setPrefix
argument_list|(
name|Constants
operator|.
name|ATTR_WSP
argument_list|)
expr_stmt|;
name|el
operator|.
name|setAttribute
argument_list|(
name|Constants
operator|.
name|ATTR_URI
argument_list|,
literal|"#"
operator|+
name|policyId
argument_list|)
expr_stmt|;
name|UnknownExtensibilityElement
name|uee
init|=
operator|new
name|UnknownExtensibilityElement
argument_list|()
decl_stmt|;
name|uee
operator|.
name|setElementType
argument_list|(
operator|new
name|QName
argument_list|(
name|Constants
operator|.
name|URI_POLICY_NS
argument_list|,
name|Constants
operator|.
name|ELEM_POLICY_REF
argument_list|)
argument_list|)
expr_stmt|;
name|uee
operator|.
name|setElement
argument_list|(
name|el
argument_list|)
expr_stmt|;
name|uee
operator|.
name|setRequired
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ext
operator|.
name|addExtensor
argument_list|(
name|uee
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|addPolicy
parameter_list|(
name|Policy
name|p
parameter_list|)
block|{
try|try
block|{
name|W3CDOMStreamWriter
name|writer
init|=
operator|new
name|W3CDOMStreamWriter
argument_list|()
decl_stmt|;
name|p
operator|.
name|serialize
argument_list|(
name|writer
argument_list|)
expr_stmt|;
name|Element
name|policyEl
init|=
name|writer
operator|.
name|getDocument
argument_list|()
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
comment|// Remove xmlns:xmlns attribute which Xerces chokes on
name|policyEl
operator|.
name|removeAttribute
argument_list|(
literal|"xmlns:xmlns"
argument_list|)
expr_stmt|;
name|UnknownExtensibilityElement
name|uee
init|=
operator|new
name|UnknownExtensibilityElement
argument_list|()
decl_stmt|;
name|uee
operator|.
name|setElementType
argument_list|(
operator|new
name|QName
argument_list|(
name|Constants
operator|.
name|URI_POLICY_NS
argument_list|,
name|Constants
operator|.
name|ELEM_POLICY
argument_list|)
argument_list|)
expr_stmt|;
name|uee
operator|.
name|setElement
argument_list|(
name|policyEl
argument_list|)
expr_stmt|;
name|ei
operator|.
name|getService
argument_list|()
operator|.
name|addExtensor
argument_list|(
name|uee
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Could not serialize policy"
argument_list|,
name|ex
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|ParserConfigurationException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Could not serialize policy"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

