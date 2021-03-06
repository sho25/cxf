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
name|corba
operator|.
name|utils
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|Binding
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
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_class
specifier|public
class|class
name|EprMetaData
block|{
specifier|private
name|Definition
name|candidateWsdlDef
decl_stmt|;
specifier|private
name|Binding
name|binding
decl_stmt|;
specifier|private
name|QName
name|serviceQName
decl_stmt|;
specifier|private
name|String
name|portName
decl_stmt|;
specifier|public
name|Binding
name|getBinding
parameter_list|()
block|{
return|return
name|binding
return|;
block|}
specifier|public
name|void
name|setBinding
parameter_list|(
name|Binding
name|b
parameter_list|)
block|{
name|binding
operator|=
name|b
expr_stmt|;
block|}
specifier|public
name|Definition
name|getCandidateWsdlDef
parameter_list|()
block|{
return|return
name|candidateWsdlDef
return|;
block|}
specifier|public
name|void
name|setCandidateWsdlDef
parameter_list|(
name|Definition
name|def
parameter_list|)
block|{
name|candidateWsdlDef
operator|=
name|def
expr_stmt|;
block|}
specifier|public
name|String
name|getPortName
parameter_list|()
block|{
return|return
name|portName
return|;
block|}
specifier|public
name|void
name|setPortName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|portName
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|QName
name|getServiceQName
parameter_list|()
block|{
return|return
name|serviceQName
return|;
block|}
specifier|public
name|void
name|setServiceQName
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
name|serviceQName
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|boolean
name|isValid
parameter_list|()
block|{
return|return
name|binding
operator|!=
literal|null
operator|&&
name|candidateWsdlDef
operator|!=
literal|null
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|String
name|ret
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|isValid
argument_list|()
condition|)
block|{
name|StringBuilder
name|b
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|'{'
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
name|binding
operator|.
name|getQName
argument_list|()
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
name|serviceQName
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
name|portName
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|'@'
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
name|candidateWsdlDef
operator|.
name|getDocumentBaseURI
argument_list|()
argument_list|)
expr_stmt|;
name|b
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
name|ret
operator|=
name|b
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
block|}
end_class

end_unit

