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
name|systest
operator|.
name|jms
operator|.
name|tx
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicBoolean
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
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
name|systest
operator|.
name|jms
operator|.
name|GreeterImplDocBase
import|;
end_import

begin_class
annotation|@
name|WebService
argument_list|(
name|endpointInterface
operator|=
literal|"org.apache.hello_world_doc_lit.Greeter"
argument_list|)
specifier|public
class|class
name|GreeterImplWithTransaction
extends|extends
name|GreeterImplDocBase
block|{
specifier|private
name|AtomicBoolean
name|flag
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|true
argument_list|)
decl_stmt|;
specifier|public
name|String
name|greetMe
parameter_list|(
name|String
name|requestType
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Reached here :"
operator|+
name|requestType
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"Bad guy"
operator|.
name|equals
argument_list|(
name|requestType
argument_list|)
condition|)
block|{
if|if
condition|(
name|flag
operator|.
name|getAndSet
argument_list|(
literal|false
argument_list|)
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Throw exception here :"
operator|+
name|requestType
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Got a bad guy call for greetMe"
argument_list|)
throw|;
block|}
else|else
block|{
name|requestType
operator|=
literal|"[Bad guy]"
expr_stmt|;
name|flag
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
return|return
literal|"Hello "
operator|+
name|requestType
return|;
block|}
block|}
end_class

end_unit

