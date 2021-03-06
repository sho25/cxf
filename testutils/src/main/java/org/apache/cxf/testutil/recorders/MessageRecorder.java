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
name|testutil
operator|.
name|recorders
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_class
specifier|public
class|class
name|MessageRecorder
block|{
specifier|private
name|OutMessageRecorder
name|outRecorder
decl_stmt|;
specifier|private
name|InMessageRecorder
name|inRecorder
decl_stmt|;
specifier|public
name|MessageRecorder
parameter_list|(
name|OutMessageRecorder
name|or
parameter_list|,
name|InMessageRecorder
name|ir
parameter_list|)
block|{
name|inRecorder
operator|=
name|ir
expr_stmt|;
name|outRecorder
operator|=
name|or
expr_stmt|;
block|}
specifier|public
name|void
name|awaitMessages
parameter_list|(
name|int
name|nExpectedOut
parameter_list|,
name|int
name|nExpectedIn
parameter_list|,
name|int
name|timeout
parameter_list|)
block|{
name|int
name|waited
init|=
literal|0
decl_stmt|;
name|int
name|nOut
init|=
literal|0
decl_stmt|;
name|int
name|nIn
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|waited
operator|<=
name|timeout
condition|)
block|{
name|nOut
operator|=
name|outRecorder
operator|.
name|getOutboundMessages
argument_list|()
operator|.
name|size
argument_list|()
expr_stmt|;
name|nIn
operator|=
name|inRecorder
operator|.
name|getInboundMessages
argument_list|()
operator|.
name|size
argument_list|()
expr_stmt|;
if|if
condition|(
name|nIn
operator|>=
name|nExpectedIn
operator|&&
name|nOut
operator|>=
name|nExpectedOut
condition|)
block|{
return|return;
block|}
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|100L
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
name|waited
operator|+=
literal|100
expr_stmt|;
block|}
if|if
condition|(
name|nExpectedIn
operator|!=
name|nIn
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
operator|(
name|nExpectedIn
operator|<
name|nIn
condition|?
literal|"excess"
else|:
literal|"shortfall"
operator|)
operator|+
literal|" of "
operator|+
name|Math
operator|.
name|abs
argument_list|(
name|nExpectedIn
operator|-
name|nIn
argument_list|)
operator|+
literal|" incoming messages"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\nMessages actually received:\n"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|byte
index|[]
argument_list|>
name|inbound
init|=
name|inRecorder
operator|.
name|getInboundMessages
argument_list|()
decl_stmt|;
for|for
control|(
name|byte
index|[]
name|b
range|:
name|inbound
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
operator|new
name|String
argument_list|(
name|b
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"----------------"
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|nExpectedOut
operator|!=
name|nOut
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
operator|(
name|nExpectedOut
operator|<
name|nOut
condition|?
literal|"excess"
else|:
literal|"shortfall"
operator|)
operator|+
literal|" of "
operator|+
name|Math
operator|.
name|abs
argument_list|(
name|nExpectedOut
operator|-
name|nOut
argument_list|)
operator|+
literal|" outgoing messages"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"\nMessages actually sent:\n"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|byte
index|[]
argument_list|>
name|outbound
init|=
name|outRecorder
operator|.
name|getOutboundMessages
argument_list|()
decl_stmt|;
for|for
control|(
name|byte
index|[]
name|b
range|:
name|outbound
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
operator|new
name|String
argument_list|(
name|b
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"----------------"
argument_list|)
expr_stmt|;
block|}
block|}
name|assertEquals
argument_list|(
literal|"Did not receive expected number of inbound messages"
argument_list|,
name|nExpectedIn
argument_list|,
name|nIn
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Did not send expected number of outbound messages"
argument_list|,
name|nExpectedOut
argument_list|,
name|nOut
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

