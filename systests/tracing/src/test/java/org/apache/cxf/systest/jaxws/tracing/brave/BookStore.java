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
name|jaxws
operator|.
name|tracing
operator|.
name|brave
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebMethod
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
name|com
operator|.
name|github
operator|.
name|kristofa
operator|.
name|brave
operator|.
name|Brave
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
name|Book
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
name|TestSpanReporter
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
name|jaxws
operator|.
name|tracing
operator|.
name|BookStoreService
import|;
end_import

begin_import
import|import
name|zipkin
operator|.
name|Constants
import|;
end_import

begin_class
annotation|@
name|WebService
argument_list|(
name|endpointInterface
operator|=
literal|"org.apache.cxf.systest.jaxws.tracing.BookStoreService"
argument_list|,
name|serviceName
operator|=
literal|"BookStore"
argument_list|)
specifier|public
class|class
name|BookStore
implements|implements
name|BookStoreService
block|{
specifier|private
specifier|final
name|Brave
name|brave
decl_stmt|;
specifier|public
name|BookStore
parameter_list|()
block|{
name|brave
operator|=
operator|new
name|Brave
operator|.
name|Builder
argument_list|(
literal|"book-store"
argument_list|)
operator|.
name|reporter
argument_list|(
operator|new
name|TestSpanReporter
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
annotation|@
name|WebMethod
specifier|public
name|Collection
argument_list|<
name|Book
argument_list|>
name|getBooks
parameter_list|()
block|{
try|try
block|{
name|brave
operator|.
name|localTracer
argument_list|()
operator|.
name|startNewSpan
argument_list|(
name|Constants
operator|.
name|LOCAL_COMPONENT
argument_list|,
literal|"Get Books"
argument_list|)
expr_stmt|;
return|return
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Book
argument_list|(
literal|"Apache CXF in Action"
argument_list|,
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|,
operator|new
name|Book
argument_list|(
literal|"Mastering Apache CXF"
argument_list|,
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
finally|finally
block|{
name|brave
operator|.
name|localTracer
argument_list|()
operator|.
name|finishSpan
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|WebMethod
specifier|public
name|int
name|removeBooks
parameter_list|()
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unable to remove books"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

