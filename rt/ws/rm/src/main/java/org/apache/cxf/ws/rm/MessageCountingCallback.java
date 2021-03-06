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

begin_comment
comment|/**  * Implementation just counts the number of messages accepted for sending and the number acknowledged, allows checking /  * waiting for completion.  */
end_comment

begin_class
specifier|public
class|class
name|MessageCountingCallback
implements|implements
name|MessageCallback
block|{
comment|/** Internal lock (rather than using this, so we can prevent any other access). */
specifier|private
name|Object
name|lock
init|=
operator|new
name|Object
argument_list|()
decl_stmt|;
specifier|private
specifier|volatile
name|int
name|countOutstanding
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|messageAccepted
parameter_list|(
name|String
name|seqId
parameter_list|,
name|long
name|msgNum
parameter_list|)
block|{
synchronized|synchronized
init|(
name|lock
init|)
block|{
name|countOutstanding
operator|++
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|messageAcknowledged
parameter_list|(
name|String
name|seqId
parameter_list|,
name|long
name|msgNum
parameter_list|)
block|{
synchronized|synchronized
init|(
name|lock
init|)
block|{
name|countOutstanding
operator|--
expr_stmt|;
if|if
condition|(
name|countOutstanding
operator|==
literal|0
condition|)
block|{
name|lock
operator|.
name|notifyAll
argument_list|()
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Get the number of messages accepted for sending which have not yet been acknowledged.      *      * @return count      */
specifier|public
name|int
name|getCountOutstanding
parameter_list|()
block|{
return|return
name|countOutstanding
return|;
block|}
comment|/**      * Wait for all accepted messages to be acknowledged.      *      * @param timeout maximum time to wait, in milliseconds (no timeout if 0)      * @return<code>true</code> if all accepted messages acknowledged,<code>false</code> if timed out      */
specifier|public
name|boolean
name|waitComplete
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
name|long
name|start
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|lock
init|)
block|{
while|while
condition|(
name|countOutstanding
operator|>
literal|0
condition|)
block|{
name|long
name|remain
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|timeout
operator|!=
literal|0
condition|)
block|{
name|remain
operator|=
name|start
operator|+
name|timeout
operator|-
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
if|if
condition|(
name|remain
operator|<=
literal|0
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
try|try
block|{
name|lock
operator|.
name|wait
argument_list|(
name|remain
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|/* ignored */
block|}
block|}
return|return
literal|true
return|;
block|}
block|}
block|}
end_class

end_unit

