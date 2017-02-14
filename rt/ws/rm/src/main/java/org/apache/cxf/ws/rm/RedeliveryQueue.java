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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
import|;
end_import

begin_interface
specifier|public
interface|interface
name|RedeliveryQueue
block|{
name|String
name|DEFAULT_BASE_REDELIVERY_INTERVAL
init|=
literal|"3000"
decl_stmt|;
name|int
name|DEFAULT_EXPONENTIAL_BACKOFF
init|=
literal|2
decl_stmt|;
comment|/**      * @param seq the sequence under consideration      * @return the number of unacknowledged messages for that sequence      */
name|int
name|countUndelivered
parameter_list|(
name|DestinationSequence
name|seq
parameter_list|)
function_decl|;
comment|/**      * @return the total number of undelivered messages in this queue      */
name|int
name|countUndelivered
parameter_list|()
function_decl|;
comment|/**      * @return true if there are no unacknowledged messages in the queue      */
name|boolean
name|isEmpty
parameter_list|()
function_decl|;
comment|/**      * Accepts a failed message for possible future redelivery.      * @param message the message context.      */
name|void
name|addUndelivered
parameter_list|(
name|Message
name|message
parameter_list|)
function_decl|;
comment|/**      * Purge all candiates for the given sequence.      *      * @param seq the sequence object      */
name|void
name|purgeAll
parameter_list|(
name|DestinationSequence
name|seq
parameter_list|)
function_decl|;
comment|/**      *      * @param seq      * @return      */
name|List
argument_list|<
name|Long
argument_list|>
name|getUndeliveredMessageNumbers
parameter_list|(
name|DestinationSequence
name|seq
parameter_list|)
function_decl|;
comment|/**      * Returns the retransmission status for the specified message.      * @param seq      * @param num      * @return      */
name|RetryStatus
name|getRedeliveryStatus
parameter_list|(
name|DestinationSequence
name|seq
parameter_list|,
name|long
name|num
parameter_list|)
function_decl|;
comment|/**      * Return the retransmission status of all the messages assigned to the sequence.      * @param seq      * @return      */
name|Map
argument_list|<
name|Long
argument_list|,
name|RetryStatus
argument_list|>
name|getRedeliveryStatuses
parameter_list|(
name|DestinationSequence
name|seq
parameter_list|)
function_decl|;
comment|/**      * Initiate resends.      */
name|void
name|start
parameter_list|()
function_decl|;
comment|/**      * Stops redelivery queue.      * @param seq      */
name|void
name|stop
parameter_list|(
name|DestinationSequence
name|seq
parameter_list|)
function_decl|;
comment|/**      * Suspends the redelivery attempts for the specified sequence      * @param seq      */
name|void
name|suspend
parameter_list|(
name|DestinationSequence
name|seq
parameter_list|)
function_decl|;
comment|/**      * Resumes the redelivery attempts for the specified sequence      * @param seq      */
name|void
name|resume
parameter_list|(
name|DestinationSequence
name|seq
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

