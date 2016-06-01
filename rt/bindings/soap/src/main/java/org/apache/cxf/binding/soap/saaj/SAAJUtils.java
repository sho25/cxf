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
operator|.
name|saaj
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

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPBody
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPFault
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPHeader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPMessage
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

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|SAAJUtils
block|{
specifier|private
name|SAAJUtils
parameter_list|()
block|{
comment|//not constructed
block|}
specifier|public
specifier|static
name|SOAPHeader
name|getHeader
parameter_list|(
name|SOAPMessage
name|m
parameter_list|)
throws|throws
name|SOAPException
block|{
try|try
block|{
return|return
name|m
operator|.
name|getSOAPHeader
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|ex
parameter_list|)
block|{
return|return
name|m
operator|.
name|getSOAPPart
argument_list|()
operator|.
name|getEnvelope
argument_list|()
operator|.
name|getHeader
argument_list|()
return|;
block|}
block|}
specifier|public
specifier|static
name|SOAPBody
name|getBody
parameter_list|(
name|SOAPMessage
name|m
parameter_list|)
throws|throws
name|SOAPException
block|{
try|try
block|{
return|return
name|m
operator|.
name|getSOAPBody
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedOperationException
name|ex
parameter_list|)
block|{
return|return
name|m
operator|.
name|getSOAPPart
argument_list|()
operator|.
name|getEnvelope
argument_list|()
operator|.
name|getBody
argument_list|()
return|;
block|}
block|}
specifier|public
specifier|static
name|void
name|setFaultCode
parameter_list|(
name|SOAPFault
name|f
parameter_list|,
name|QName
name|code
parameter_list|)
throws|throws
name|SOAPException
block|{
try|try
block|{
name|f
operator|.
name|setFaultCode
argument_list|(
name|code
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|int
name|count
init|=
literal|1
decl_stmt|;
name|String
name|pfx
init|=
literal|"fc1"
decl_stmt|;
while|while
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|f
operator|.
name|getNamespaceURI
argument_list|(
name|pfx
argument_list|)
argument_list|)
condition|)
block|{
name|count
operator|++
expr_stmt|;
name|pfx
operator|=
literal|"fc"
operator|+
name|count
expr_stmt|;
block|}
if|if
condition|(
name|code
operator|.
name|getNamespaceURI
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|code
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
name|f
operator|.
name|addNamespaceDeclaration
argument_list|(
name|pfx
argument_list|,
name|code
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|f
operator|.
name|addNamespaceDeclaration
argument_list|(
name|pfx
argument_list|,
name|f
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|f
operator|.
name|setFaultCode
argument_list|(
name|pfx
operator|+
literal|":"
operator|+
name|code
operator|.
name|getLocalPart
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|SOAPElement
name|adjustPrefix
parameter_list|(
name|SOAPElement
name|e
parameter_list|,
name|String
name|prefix
parameter_list|)
block|{
if|if
condition|(
name|prefix
operator|==
literal|null
condition|)
block|{
name|prefix
operator|=
literal|""
expr_stmt|;
block|}
try|try
block|{
name|String
name|s
init|=
name|e
operator|.
name|getPrefix
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|prefix
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|e
operator|.
name|setPrefix
argument_list|(
name|prefix
argument_list|)
expr_stmt|;
name|e
operator|.
name|removeNamespaceDeclaration
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//likely old old version of SAAJ, we'll just try our best
block|}
return|return
name|e
return|;
block|}
block|}
end_class

end_unit

