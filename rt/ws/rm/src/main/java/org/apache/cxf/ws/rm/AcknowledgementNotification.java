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
name|javax
operator|.
name|management
operator|.
name|Notification
import|;
end_import

begin_comment
comment|/**  * Notification of a message acknowledgment for a source sequence.  */
end_comment

begin_class
specifier|public
class|class
name|AcknowledgementNotification
extends|extends
name|Notification
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|7809325584426123035L
decl_stmt|;
specifier|private
specifier|final
name|String
name|sequenceId
decl_stmt|;
specifier|private
specifier|final
name|long
name|messageNumber
decl_stmt|;
specifier|public
name|AcknowledgementNotification
parameter_list|(
name|Object
name|source
parameter_list|,
name|long
name|seq
parameter_list|,
name|String
name|sid
parameter_list|,
name|long
name|msgnum
parameter_list|)
block|{
name|super
argument_list|(
name|ManagedRMEndpoint
operator|.
name|ACKNOWLEDGEMENT_NOTIFICATION
argument_list|,
name|source
argument_list|,
name|seq
argument_list|)
expr_stmt|;
name|sequenceId
operator|=
name|sid
expr_stmt|;
name|messageNumber
operator|=
name|msgnum
expr_stmt|;
block|}
specifier|public
name|String
name|getSequenceId
parameter_list|()
block|{
return|return
name|sequenceId
return|;
block|}
specifier|public
name|long
name|getMessageNumber
parameter_list|()
block|{
return|return
name|messageNumber
return|;
block|}
block|}
end_class

end_unit

