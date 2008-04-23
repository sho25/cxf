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
name|binding
operator|.
name|soap
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
comment|/**  * Represents the SOAP 1.2 version  *   */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|Soap12
implements|implements
name|SoapVersion
block|{
specifier|public
specifier|static
specifier|final
name|String
name|SOAP_NAMESPACE
init|=
literal|"http://www.w3.org/2003/05/soap-envelope"
decl_stmt|;
specifier|private
specifier|static
name|Soap12
name|instance
init|=
operator|new
name|Soap12
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|double
name|version
init|=
literal|1.2
decl_stmt|;
specifier|private
specifier|final
name|String
name|namespace
init|=
name|SOAP_NAMESPACE
decl_stmt|;
specifier|private
specifier|final
name|String
name|prefix
init|=
literal|"soap"
decl_stmt|;
specifier|private
specifier|final
name|String
name|noneRole
init|=
name|namespace
operator|+
literal|"/role/none"
decl_stmt|;
specifier|private
specifier|final
name|String
name|ultimateReceiverRole
init|=
name|namespace
operator|+
literal|"/role/ultimateReceiver"
decl_stmt|;
specifier|private
specifier|final
name|String
name|nextRole
init|=
name|namespace
operator|+
literal|"/role/next"
decl_stmt|;
specifier|private
specifier|final
name|String
name|soapEncodingStyle
init|=
literal|"http://www.w3.org/2003/05/soap-encoding"
decl_stmt|;
specifier|private
specifier|final
name|QName
name|envelope
init|=
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
literal|"Envelope"
argument_list|,
name|prefix
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|QName
name|header
init|=
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
literal|"Header"
argument_list|,
name|prefix
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|QName
name|body
init|=
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
literal|"Body"
argument_list|,
name|prefix
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|QName
name|fault
init|=
operator|new
name|QName
argument_list|(
name|namespace
argument_list|,
literal|"Fault"
argument_list|,
name|prefix
argument_list|)
decl_stmt|;
specifier|private
name|Soap12
parameter_list|()
block|{
comment|// Singleton
block|}
specifier|public
specifier|static
name|Soap12
name|getInstance
parameter_list|()
block|{
return|return
name|instance
return|;
block|}
specifier|public
name|double
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
specifier|public
name|String
name|getNamespace
parameter_list|()
block|{
return|return
name|namespace
return|;
block|}
specifier|public
name|String
name|getPrefix
parameter_list|()
block|{
return|return
name|prefix
return|;
block|}
specifier|public
name|QName
name|getEnvelope
parameter_list|()
block|{
return|return
name|envelope
return|;
block|}
specifier|public
name|QName
name|getHeader
parameter_list|()
block|{
return|return
name|header
return|;
block|}
specifier|public
name|QName
name|getBody
parameter_list|()
block|{
return|return
name|body
return|;
block|}
specifier|public
name|QName
name|getFault
parameter_list|()
block|{
return|return
name|fault
return|;
block|}
specifier|public
name|String
name|getSoapEncodingStyle
parameter_list|()
block|{
return|return
name|soapEncodingStyle
return|;
block|}
comment|// Role URIs
comment|// -------------------------------------------------------------------------
specifier|public
name|String
name|getNoneRole
parameter_list|()
block|{
return|return
name|noneRole
return|;
block|}
specifier|public
name|String
name|getUltimateReceiverRole
parameter_list|()
block|{
return|return
name|ultimateReceiverRole
return|;
block|}
specifier|public
name|String
name|getNextRole
parameter_list|()
block|{
return|return
name|nextRole
return|;
block|}
specifier|public
name|String
name|getAttrNameRole
parameter_list|()
block|{
return|return
literal|"role"
return|;
block|}
specifier|public
name|String
name|getAttrNameMustUnderstand
parameter_list|()
block|{
return|return
literal|"mustUnderstand"
return|;
block|}
specifier|public
name|QName
name|getReceiver
parameter_list|()
block|{
return|return
operator|new
name|QName
argument_list|(
name|SOAP_NAMESPACE
argument_list|,
literal|"Receiver"
argument_list|)
return|;
block|}
specifier|public
name|QName
name|getSender
parameter_list|()
block|{
return|return
operator|new
name|QName
argument_list|(
name|SOAP_NAMESPACE
argument_list|,
literal|"Sender"
argument_list|)
return|;
block|}
specifier|public
name|QName
name|getMustUnderstand
parameter_list|()
block|{
return|return
operator|new
name|QName
argument_list|(
name|SOAP_NAMESPACE
argument_list|,
literal|"MustUnderstand"
argument_list|)
return|;
block|}
specifier|public
name|QName
name|getVersionMismatch
parameter_list|()
block|{
return|return
operator|new
name|QName
argument_list|(
name|SOAP_NAMESPACE
argument_list|,
literal|"VersionMismatch"
argument_list|)
return|;
block|}
specifier|public
name|QName
name|getDateEncodingUnknown
parameter_list|()
block|{
return|return
operator|new
name|QName
argument_list|(
name|SOAP_NAMESPACE
argument_list|,
literal|"DataEncodingUnknown"
argument_list|)
return|;
block|}
specifier|public
name|String
name|getContentType
parameter_list|()
block|{
return|return
literal|"application/soap+xml"
return|;
block|}
block|}
end_class

end_unit

