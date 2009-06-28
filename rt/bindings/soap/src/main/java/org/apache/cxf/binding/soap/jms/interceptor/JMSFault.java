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
name|jms
operator|.
name|interceptor
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
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|JMSFault
extends|extends
name|Exception
block|{
specifier|private
name|JMSFaultType
name|jmsFaultType
decl_stmt|;
specifier|private
name|boolean
name|sender
decl_stmt|;
specifier|private
name|Object
name|detail
decl_stmt|;
specifier|public
name|JMSFault
parameter_list|(
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
specifier|public
name|JMSFault
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
comment|/**      * * @return Returns the jmsFaultType.      */
specifier|public
name|JMSFaultType
name|getJmsFaultType
parameter_list|()
block|{
return|return
name|jmsFaultType
return|;
block|}
comment|/**      * @param jmsFaultType The jmsFaultType to set.      */
specifier|public
name|void
name|setJmsFaultType
parameter_list|(
name|JMSFaultType
name|jmsFaultType
parameter_list|)
block|{
name|this
operator|.
name|jmsFaultType
operator|=
name|jmsFaultType
expr_stmt|;
block|}
comment|/**      * * @return Returns the sender.      */
specifier|public
name|boolean
name|isSender
parameter_list|()
block|{
return|return
name|sender
return|;
block|}
comment|/**      * @param sender The sender to set.      */
specifier|public
name|void
name|setSender
parameter_list|(
name|boolean
name|sender
parameter_list|)
block|{
name|this
operator|.
name|sender
operator|=
name|sender
expr_stmt|;
block|}
comment|/**      * * @return Returns the detail.      */
specifier|public
name|Object
name|getDetail
parameter_list|()
block|{
return|return
name|detail
return|;
block|}
comment|/**      * @param detail The detail to set.      */
specifier|public
name|void
name|setDetail
parameter_list|(
name|Object
name|detail
parameter_list|)
block|{
name|this
operator|.
name|detail
operator|=
name|detail
expr_stmt|;
block|}
specifier|public
name|QName
name|getSubCode
parameter_list|()
block|{
return|return
name|jmsFaultType
operator|.
name|faultCode
return|;
block|}
specifier|public
name|String
name|getReason
parameter_list|()
block|{
return|return
name|getMessage
argument_list|()
return|;
block|}
block|}
end_class

end_unit

