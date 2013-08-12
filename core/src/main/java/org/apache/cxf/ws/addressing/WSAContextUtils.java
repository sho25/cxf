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
name|addressing
package|;
end_package

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

begin_comment
comment|/**  * Holder for utility methods relating to contexts.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|WSAContextUtils
block|{
specifier|public
specifier|static
specifier|final
name|String
name|REPLYTO_PROPERTY
init|=
literal|"org.apache.cxf.ws.addressing.replyto"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|USING_PROPERTY
init|=
literal|"org.apache.cxf.ws.addressing.using"
decl_stmt|;
comment|/**      * Prevents instantiation.      */
specifier|private
name|WSAContextUtils
parameter_list|()
block|{     }
comment|/**      * Retrieve UsingAddressing override flag from the context      *      * @param message the current message      * @return true if UsingAddressing should be overridden      */
specifier|public
specifier|static
name|boolean
name|retrieveUsingAddressing
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Boolean
name|override
init|=
operator|(
name|Boolean
operator|)
name|message
operator|.
name|get
argument_list|(
name|USING_PROPERTY
argument_list|)
decl_stmt|;
return|return
name|override
operator|==
literal|null
operator|||
name|override
operator|.
name|booleanValue
argument_list|()
return|;
block|}
block|}
end_class

end_unit

