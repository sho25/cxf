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
name|jaxrs
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletOutputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|WriteListener
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|http
operator|.
name|AbstractHTTPDestination
import|;
end_import

begin_class
specifier|public
class|class
name|ServletOutputStreamFilter
extends|extends
name|ServletOutputStream
block|{
specifier|private
name|Message
name|m
decl_stmt|;
specifier|private
name|ServletOutputStream
name|os
decl_stmt|;
specifier|public
name|ServletOutputStreamFilter
parameter_list|(
name|ServletOutputStream
name|os
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
name|this
operator|.
name|os
operator|=
name|os
expr_stmt|;
name|this
operator|.
name|m
operator|=
name|m
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|int
name|b
parameter_list|)
throws|throws
name|IOException
block|{
name|setComittedStatus
argument_list|()
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|byte
index|[]
name|b
parameter_list|)
throws|throws
name|IOException
block|{
name|setComittedStatus
argument_list|()
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|byte
index|[]
name|b
parameter_list|,
name|int
name|off
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|IOException
block|{
name|setComittedStatus
argument_list|()
expr_stmt|;
name|os
operator|.
name|write
argument_list|(
name|b
argument_list|,
name|off
argument_list|,
name|len
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|setComittedStatus
parameter_list|()
block|{
name|m
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|AbstractHTTPDestination
operator|.
name|RESPONSE_COMMITED
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
comment|//Servlet 3.1 additions
specifier|public
name|boolean
name|isReady
parameter_list|()
block|{
return|return
name|os
operator|.
name|isReady
argument_list|()
return|;
block|}
specifier|public
name|void
name|setWriteListener
parameter_list|(
name|WriteListener
name|writeListener
parameter_list|)
block|{
name|os
operator|.
name|setWriteListener
argument_list|(
name|writeListener
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

