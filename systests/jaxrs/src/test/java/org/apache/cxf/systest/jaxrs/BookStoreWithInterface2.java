begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|jaxrs
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|PreDestroy
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Context
import|;
end_import

begin_class
specifier|public
class|class
name|BookStoreWithInterface2
extends|extends
name|BookStoreStorage
implements|implements
name|BookInterface
block|{
specifier|private
name|ServletContext
name|servletContext
decl_stmt|;
specifier|public
name|BookStoreWithInterface2
parameter_list|()
block|{
name|Book
name|book
init|=
operator|new
name|Book
argument_list|()
decl_stmt|;
name|book
operator|.
name|setId
argument_list|(
name|bookId
argument_list|)
expr_stmt|;
name|book
operator|.
name|setName
argument_list|(
literal|"CXF in Action"
argument_list|)
expr_stmt|;
name|books
operator|.
name|put
argument_list|(
name|book
operator|.
name|getId
argument_list|()
argument_list|,
name|book
argument_list|)
expr_stmt|;
block|}
specifier|public
name|BookStoreWithInterface2
parameter_list|(
annotation|@
name|Context
name|ServletContext
name|scontext
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|this
operator|.
name|servletContext
operator|=
name|scontext
expr_stmt|;
block|}
annotation|@
name|PreDestroy
specifier|public
name|void
name|preDestroy
parameter_list|()
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"PreDestroy called"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Book
name|getThatBook
parameter_list|(
name|Long
name|id
parameter_list|,
name|String
name|s
parameter_list|)
throws|throws
name|BookNotFoundFault
block|{
if|if
condition|(
name|servletContext
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|()
throw|;
block|}
if|if
condition|(
operator|!
name|id
operator|.
name|toString
argument_list|()
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|()
throw|;
block|}
return|return
name|doGetBook
argument_list|(
name|id
argument_list|)
return|;
block|}
specifier|public
name|Book
name|getThatBook
parameter_list|(
name|Long
name|id
parameter_list|)
throws|throws
name|BookNotFoundFault
block|{
if|if
condition|(
name|servletContext
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|()
throw|;
block|}
return|return
name|doGetBook
argument_list|(
name|id
argument_list|)
return|;
block|}
specifier|private
name|Book
name|doGetBook
parameter_list|(
name|Long
name|id
parameter_list|)
throws|throws
name|BookNotFoundFault
block|{
comment|//System.out.println("----invoking getBook with id: " + id);
name|Book
name|book
init|=
name|books
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|book
operator|!=
literal|null
condition|)
block|{
return|return
name|book
return|;
block|}
else|else
block|{
name|BookNotFoundDetails
name|details
init|=
operator|new
name|BookNotFoundDetails
argument_list|()
decl_stmt|;
name|details
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|BookNotFoundFault
argument_list|(
name|details
argument_list|)
throw|;
block|}
block|}
specifier|public
name|Book
name|getThatBook
parameter_list|()
throws|throws
name|BookNotFoundFault
block|{
if|if
condition|(
name|servletContext
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|()
throw|;
block|}
return|return
name|books
operator|.
name|get
argument_list|(
literal|123L
argument_list|)
return|;
block|}
specifier|public
name|Book
name|echoBook
parameter_list|(
name|Book
name|b
parameter_list|)
block|{
return|return
name|b
return|;
block|}
block|}
end_class

end_unit

